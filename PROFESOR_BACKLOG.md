# Backlog — Implementare Profesor

Pattern: copiezi structura `student/` și adaptezi. `UserType.PROFESOR` deja există în enum (nu modifici).

**Convenție:** fiecare Story = un commit separat. Task-uri = pași în ordine. Bifează cu `[x]` ce ai terminat.

---

## EPIC — Profesorul ca al doilea rol de user

Un User cu `userType = PROFESOR` are propria entitate `Profesor` (paralel cu `Student`), poate fi asignat unor cursuri și are propriile permisiuni.

```
User (firstName, lastName, email, password, userType)
  ├─ Student (1-1 opțional, dacă userType = STUDENT)
  └─ Profesor (1-1 opțional, dacă userType = PROFESOR)  ← NOU

Course
  └─ profesor (ManyToOne, opțional)                    ← NOU
```

---

## Story 1 — Model + relație cu User

**Acceptance:** există entitatea `Profesor` în package-ul propriu, cu OneToOne către `User` (oglindă cu `Student`).

- [ ] **1.1** Creează folderul `src/main/java/ro/mycode/sebischool/profesor/` cu subfolderele: `model/`, `dtos/`, `exceptions/`, `mapper/`, `repository/`, `controller/`, `service/commandService/`, `service/queryService/`.
- [ ] **1.2** Creează `profesor/model/Profesor.java` (template: copiază `student/model/Student.java`):
  - Câmpuri specifice profesor: `String specializare` (NotBlank), `int experientaAni` (NotNull, ≥ 0), `String departament` (NotBlank).
  - **NU** copia `Book`-uri și `Enrolment`-uri din Student — Profesor are doar relația cu Course (vine în Story 2).
  - Păstrează OneToOne cu User: `@JoinColumn(name = "user_id", unique = true, nullable = false)`.
  - `@Table(name = "profesor")`.
- [ ] **1.3** În `users/model/User.java`, adaugă oglinda:
  ```java
  @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
  private Profesor profesor;
  ```
- [ ] **1.4** Verify: `./mvnw clean compile` → BUILD SUCCESS, tabela `profesor` apare în baza dev după boot.

---

## Story 2 — Relația Course ↔ Profesor

**Acceptance:** un Course poate avea un Profesor asignat; un Profesor poate vedea ce cursuri predă.

- [ ] **2.1** În `course/model/Course.java`, adaugă:
  ```java
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "profesor_id")
  private Profesor profesor;
  ```
  (opțional la creare, profesorul poate fi asignat ulterior)
- [ ] **2.2** În `profesor/model/Profesor.java`, adaugă oglinda:
  ```java
  @OneToMany(mappedBy = "profesor", fetch = FetchType.LAZY)
  private Set<Course> courses = new HashSet<>();
  ```
- [ ] **2.3** Verify: în baza dev, `course.profesor_id` e nullable FK către `profesor.id`.

---

## Story 3 — DTO-uri

**Acceptance:** există request/response/patch DTOs paralel cu cele din `student/dtos/`.

- [ ] **3.1** `profesor/dtos/ProfesorRequest.java` — clasic POJO cu `@NotBlank firstName, lastName, email`, `@NotBlank specializare, departament`, `@Min(0) experientaAni`.
- [ ] **3.2** `profesor/dtos/ProfesorPatchRequest.java` — record cu câmpuri opționale (nullable): `firstName, lastName, email, specializare, departament, experientaAni`. (model: `StudentPatchRequest`).
- [ ] **3.3** `profesor/dtos/ProfesorSummaryResponse.java` — record cu `id, firstName, lastName, email, specializare, departament, experientaAni`. NU include `courses` (evită cycle în JSON).
- [ ] **3.4** `profesor/dtos/ProfesorWithCoursesResponse.java` — record care include și lista de cursuri (nume + departament), pentru endpoint-uri detaliate.

---

## Story 4 — Repository

- [ ] **4.1** `profesor/repository/ProfesorRepository.java`:
  ```java
  public interface ProfesorRepository extends JpaRepository<Profesor, Long> {
      Optional<Profesor> findByUserEmail(String email);
      boolean existsByUserEmail(String email);
  }
  ```

---

## Story 5 — Exceptions

- [ ] **5.1** `profesor/exceptions/ProfesorNotFoundException.java` — `extends RuntimeException`, mesaj „Profesor not found" (template: `StudentNotFoundException`).
- [ ] **5.2** `profesor/exceptions/ProfesorAlreadyExistsException.java` — mesaj „Profesor already exists".
- [ ] **5.3** În `system/exceptions/GlobalExceptionsHandler.java`:
  - Adaugă `ProfesorNotFoundException.class` în `handleBadRequest` (alături de `StudentNotFoundException`, returns 404).
  - Adaugă `ProfesorAlreadyExistsException.class` în `handleConflict` (returns 409).

---

## Story 6 — Mapper

- [ ] **6.1** `profesor/mapper/ProfesorMapper.java` (template: `student/mapper/StudentMapper.java`):
  - `static ProfesorSummaryResponse toSummary(Profesor p)` — citește din `p.getUser()` pentru identitate, din `p` pentru câmpurile specifice.
  - `static ProfesorWithCoursesResponse toWithCourses(Profesor p)` — adaugă lista de cursuri (map Course → id + name + departament).
  - **NU** crea metodă de la Request → Entity de aici. Service-ul construiește direct, ca în StudentCommandService.

---

## Story 7 — Query Service

- [ ] **7.1** Interfața `profesor/service/queryService/ProfesorQueryService.java`:
  ```java
  List<ProfesorSummaryResponse> getAllProfesori();
  ProfesorWithCoursesResponse getProfesorById(Long id);
  ProfesorSummaryResponse getProfesorByEmail(String email);
  ```
- [ ] **7.2** `ProfesorQueryServiceImpl.java` — implementare cu `@Component`, `@Transactional(readOnly = true)`. Aruncă `ProfesorNotFoundException` când lipsește.

---

## Story 8 — Command Service

- [ ] **8.1** Interfața `profesor/service/commandService/ProfesorCommandService.java`:
  ```java
  ProfesorSummaryResponse updateProfesor(Long id, ProfesorRequest req);
  ProfesorSummaryResponse updatePatchProfesor(Long id, ProfesorPatchRequest req);
  void deleteProfesor(Long id);
  ProfesorSummaryResponse assignCourse(Long profesorId, Long courseId);
  ProfesorSummaryResponse unassignCourse(Long profesorId, Long courseId);
  ```
  **NU** ai metodă `addProfesor` separată — vine prin `/auth/register` cu `userType=PROFESOR` (Story 11). Replicăm `StudentCommandServiceImpl` care aruncă `UnsupportedOperationException` pe `addStudent`.
- [ ] **8.2** `ProfesorCommandServiceImpl.java`:
  - `updateProfesor` — `findById` → re-set toate câmpurile (firstName/lastName/email pe User, specializare/departament/experientaAni pe Profesor) → save.
  - `updatePatchProfesor` — același pattern ca `updatePatchStudent` (null-check fiecare câmp).
  - `deleteProfesor` — `existsById` check → throw → `deleteById`. Returnează `void`.
  - `assignCourse` — `findById(profesorId)` și `findById(courseId)`, `course.setProfesor(profesor)`, save course.
  - `unassignCourse` — find course, `course.setProfesor(null)`, save.

---

## Story 9 — Controller

**Acceptance:** există endpoint-uri REST sub `/api/v2/profesori`, cu `@PreAuthorize` corect setat.

- [ ] **9.1** `profesor/controller/ProfesorController.java` (template: `StudentController`):
  ```java
  @RestController
  @RequestMapping("/api/v2/profesori")
  ```
- [ ] **9.2** Endpoint-uri și permisiuni:
  | Method | Path | Permission | Service call |
  |---|---|---|---|
  | GET | `/` | `USER_VIEW` | `getAllProfesori` |
  | GET | `/{id}` | `USER_VIEW` | `getProfesorById` |
  | PUT | `/{id}` | `USER_EDIT` | `updateProfesor` |
  | PATCH | `/{id}` | `USER_EDIT` | `updatePatchProfesor` |
  | DELETE | `/{id}` | `USER_DELETE` | `deleteProfesor` → `noContent().build()` |
  | POST | `/{profesorId}/courses/{courseId}` | `COURSE_MANAGE` | `assignCourse` |
  | DELETE | `/{profesorId}/courses/{courseId}` | `COURSE_MANAGE` | `unassignCourse` |
- [ ] **9.3** Adnotează FIECARE endpoint cu `@PreAuthorize("hasAuthority('<PERM>')")` — fără asta orice user autentificat poate apela.

---

## Story 10 — Permisiuni pentru PROFESOR

**Acceptance:** când un user se înregistrează cu `userType=PROFESOR`, primește un set diferit de permisiuni față de STUDENT.

- [ ] **10.1** În `users/security/UserPermissions.java`, verifică că ai (adaugă dacă lipsesc):
  - `COURSE_VIEW`, `COURSE_MANAGE`
  - `USER_VIEW`, `USER_EDIT`
  - `STUDENT_VIEW` (profesorul vede studenții la cursurile lui)
- [ ] **10.2** În `auth/authService/AuthServiceImpl.java`, metoda `permissionsForType(UserType type)`:
  ```java
  case PROFESOR -> Set.of(
      UserPermissions.COURSE_VIEW,
      UserPermissions.COURSE_MANAGE,
      UserPermissions.USER_VIEW,
      UserPermissions.STUDENT_VIEW
      // NU primește USER_DELETE — doar admin/sef catedră ar trebui
  );
  ```
- [ ] **10.3** Verify: după register cu `userType=PROFESOR`, JWT-ul decodat conține perm-urile de mai sus.

---

## Story 11 — Register pentru PROFESOR

**Acceptance:** `POST /api/v2/auth/register` cu `userType=PROFESOR` creează User + Profesor în aceeași tranzacție.

- [ ] **11.1** În DTO-ul `auth/dtos/RegisterRequest.java` (sau cum se cheamă la tine), verifică că accepți `userType` și câmpuri specifice profesor (`specializare`, `departament`, `experientaAni`). Acceptă-le ca opționale dacă `userType=STUDENT`.
- [ ] **11.2** În `AuthServiceImpl.register(...)`:
  - După ce salvezi User-ul, dacă `userType == PROFESOR`:
    ```java
    Profesor profesor = Profesor.builder()
        .specializare(req.getSpecializare())
        .departament(req.getDepartament())
        .experientaAni(req.getExperientaAni())
        .user(savedUser)
        .build();
    profesorRepository.save(profesor);
    ```
  - Injectează `ProfesorRepository` în constructorul `AuthServiceImpl`.
- [ ] **11.3** Test manual cu Postman:
  ```
  POST /api/v2/auth/register
  {
    "firstName": "Ion",
    "lastName": "Popescu",
    "email": "ion@test.ro",
    "password": "Parola123!",
    "userType": "PROFESOR",
    "specializare": "Matematica",
    "departament": "Stiinte exacte",
    "experientaAni": 10
  }
  ```
  Verifică: în DB apare 1 row în `user` + 1 row în `profesor` + 4 row-uri în `user_permissions`.

---

## Story 12 — Endpoint pentru a vedea studenții profesorului

**Acceptance:** Profesor poate vedea studenții care s-au înscris la cursurile lui.

- [ ] **12.1** În `EnrolmentQueryService`, adaugă `List<StudentSummaryResponse> getStudentsByProfesorId(Long profesorId);`.
- [ ] **12.2** Implementare: query custom în `EnrolmentRepository` — `findByCourseProfesorId(Long profesorId)` și mapează la `StudentSummaryResponse`.
- [ ] **12.3** În `ProfesorController` (sau `EnrolmentController`), endpoint:
  ```java
  @GetMapping("/{profesorId}/students")
  @PreAuthorize("hasAuthority('STUDENT_VIEW')")
  public ResponseEntity<List<StudentSummaryResponse>> getStudentsOfProfesor(@PathVariable Long profesorId) { ... }
  ```

---

## Story 13 — Tests

**Acceptance:** există minim 5 teste de integrare care acoperă fluxul complet.

- [ ] **13.1** `ProfesorCommandServiceImplTest` — folosește `@DataJpaTest` cu `@Import(ProfesorCommandServiceImpl.class)` + H2.
  - `updateProfesor_ok`
  - `updateProfesor_notFound_throws`
  - `assignCourse_ok` — verifică `course.getProfesor().getId() == profesorId`
  - `unassignCourse_ok` — verifică `course.getProfesor() == null`
  - `deleteProfesor_cascadesCourseToNull` — profesorul șters, cursul rămâne cu `profesor = null`
- [ ] **13.2** `ProfesorControllerTest` cu `@WebMvcTest` + mock-uri:
  - `POST /{id}/courses/{cid}` cu user fără `COURSE_MANAGE` → 403
  - Aceeași cale cu permisiunea corectă → 200

---

## Definition of Done (pe TOATE story-urile)

- [ ] `./mvnw clean compile` → BUILD SUCCESS
- [ ] `./mvnw test` → toate testele verzi
- [ ] Endpoint-urile funcționează în Postman (register, list, assign course, view students)
- [ ] Niciun endpoint nu are TODO sau `return null` (cu excepția unde e explicit OK)
- [ ] `@PreAuthorize` pe FIECARE endpoint care modifică date
- [ ] Niciun warning de compilare

---

## Ordine recomandată de implementare

1. Story **1, 2, 3, 4, 5, 6** — fundație (model + repo + DTO + exceptions + mapper). Commit-uri scurte, una pe story.
2. Story **7, 8** — service-uri. Aici încep să apară problemele de design (asigneaza/dezasigneaza).
3. Story **9, 10, 11** — controller + auth (integrarea cu sistemul existent).
4. Story **12** — feature suplimentar (raport studenți).
5. Story **13** — teste la final, dar IDEAL în paralel cu story-urile (TDD).

**Sfat:** după fiecare story → `git commit -m "story <N>: <descriere>"`. Dacă pici la o story, întoarce-te la commit-ul precedent și reia.
