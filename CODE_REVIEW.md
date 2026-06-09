# Code Review — `feat/implement-professor-feature`

Reviewer: Bogdan (instructor)
Data: 2026-06-09
Scope: feature-ul **Profesor** conform `PROFESOR_BACKLOG.md`

> **Cum să citești:** problemele sunt grupate pe severitate. CRITIC = blochează feature-ul / spargere API. HIGH = abateri serioase de la backlog. MEDIUM = bug-uri tăcute. LOW/NIT = polish. Pentru fiecare punct ai `fișier:linie` și explicație scurtă cu **De ce contează** + **Cum repari**.

---

## 🔴 CRITICE (de rezolvat înainte de merge)

### C1. PATCH endpoint este identic cu PUT — bug ascuns
**`professor/controller/ProfessorControler.java:48-53`**

```java
@PatchMapping("/{id}")
@PreAuthorize("hasAuthority('USER_EDIT')")
public ResponseEntity<ProfessorResponse> updatePatch(@PathVariable Long id,
                                                     @RequestBody ProfessorRequest professorRequest) {  // ❌
    ProfessorResponse professorResponse = professorCommandService.updateProfessor(id, professorRequest); // ❌
    ...
}
```

PATCH primește `ProfessorRequest` (toate câmpurile obligatorii prin `@NotBlank`) și apelează `updateProfessor`, nu `patchProfessor`. Practic PATCH = PUT.

**De ce contează:** scopul PATCH e update parțial. Acum dacă clientul trimite doar `specialty`, primește 400 pentru că `firstName` lipsește.
**Cum repari:** `@RequestBody ProfessorPatchRequest` + apelează `professorCommandService.patchProfessor(...)`.

---

### C2. Lipsește `GET /{id}` (Story 9.2)
**`professor/controller/ProfessorControler.java`**

Backlog cere un endpoint pentru a aduce un profesor individual cu cursurile lui. Acum nu se poate citi profesor după id — doar lista completă (`GET /`) sau studenții profesorului (`GET /{id}/students`).

**Cum repari:**
```java
@GetMapping("/{id}")
@PreAuthorize("hasAuthority('USER_VIEW')")
public ResponseEntity<ProfessorWithCoursesResponse> getById(@PathVariable Long id) { ... }
```

---

### C3. Permisiune greșită pe `GET /` (citire ≠ edit)
**`professor/controller/ProfessorControler.java:55-56`**

```java
@GetMapping("")
@PreAuthorize("hasAuthority('USER_EDIT')")  // ❌ ar trebui USER_VIEW
```

**De ce contează:** un user care are doar `USER_VIEW` (cum e profesorul însuși, sau studentul care vrea să vadă lista) nu poate citi lista. Inconsistent cu `StudentController`.

---

### C4. `@Valid` lipsește pe `@RequestBody`
**`professor/controller/ProfessorControler.java:43, 50`**

```java
public ResponseEntity<ProfessorResponse> updateProfessor(@PathVariable Long id,
                                                         @RequestBody ProfessorRequest professorRequest) { // ❌ fără @Valid
```

**De ce contează:** toate `@NotBlank`/`@Min` din DTO sunt **complet ignorate**. Poți trimite `{"firstName":""}` și trece prin validare.
**Cum repari:** `@Valid @RequestBody ProfessorRequest professorRequest`.

---

### C5. `addProfessor` are implementare completă în loc de `UnsupportedOperationException`
**`professor/service/ProfessorCommandServiceImpl.java:34-47`**

Backlog Story 8.1 spune explicit: **„NU ai metodă `addProfessor` separată — vine prin `/auth/register` cu `userType=PROFESOR`. Replicăm `StudentCommandServiceImpl` care aruncă `UnsupportedOperationException` pe `addStudent`"**.

Acum ai două căi paralele de a crea profesor (auth + addProfessor) — una creează `User+Professor`, cealaltă creează doar `Professor` orfan (fără User).

**Cum repari:**
```java
@Override
public ProfessorResponse addProfessor(ProfessorRequest req) {
    throw new UnsupportedOperationException("Use /api/v2/auth/register with userType=PROFESOR");
}
```

---

### C6. `Professor` entity duplică `firstName`/`lastName` cu `User`
**`professor/model/Professor.java:27-31, 42-43`**

```java
@NotBlank private String firstName;   // ❌ duplicat cu User.firstName
@NotBlank private String lastName;    // ❌ duplicat cu User.lastName
...
@OneToOne private User user;          // ❌ lipsește @JoinColumn(name="user_id", unique=true, nullable=false)
```

Backlog Story 1.2 cere DOAR câmpurile specifice profesor (`specializare`, `experientaAni`, `departament`) + relație OneToOne. Identitatea (firstName, lastName, email) **trăiește pe User**, nu pe Professor.

**De ce contează:**
1. La register, ce salvezi în `Professor.firstName`? Va fi `null` (vezi `AuthServiceImpl` — nu setează aceste câmpuri pe Professor). → `@NotBlank` cade la insert.
2. Dacă editezi numele pe `User`, `Professor.firstName` rămâne stale → date inconsistente.
3. `@OneToOne` fără `@JoinColumn(unique=true, nullable=false)` permite doi profesori cu același user.

**Cum repari:** șterge `firstName`/`lastName` de pe `Professor`; în mapper citește din `professor.getUser().getFirstName()`. Adaugă `@JoinColumn(name="user_id", unique=true, nullable=false)` pe relația User.

---

## 🟠 HIGH (feature incomplet vs. backlog)

### H1. `updateProfessor` nu actualizează `yearExperience`
**`professor/service/ProfessorCommandServiceImpl.java:51-64`**

```java
professor.setFirstName(...);
professor.setLastName(...);
professor.setDepartament(...);
professor.setSpecialty(...);
// ❌ yearExperience nu se setează
```

Adaugă `professor.setYearExperience(professorRequest.yearExperience());`.

---

### H2. `ProfessorPatchRequest` are doar `specialty`
**`professor/dtos/ProfessorPatchRequest.java`**

Backlog Story 3.2 cere: `firstName, lastName, email, specializare, departament, experientaAni` — toate opționale. Acum doar `specialty`.

Și în `patchProfessor` (`ProfessorCommandServiceImpl.java:68-78`) trebuie pattern null-check pentru fiecare câmp (ca în `updatePatchStudent`).

---

### H3. `ProfessorMapper.toDto()` omite `yearExperience`
**`professor/mapper/ProfessorMapper.java`** (toDto)

Clientul primește răspuns fără `yearExperience` — câmp lipsă din JSON deși există în DTO.

---

### H4. Lipsesc `ProfessorWithCoursesResponse` + metodele de mapper
**Story 3.4 + Story 6.1**

Nu există DTO-ul cu lista de cursuri (pentru `GET /{id}` — vezi C2). Mapper-ul are doar `toDto`, lipsesc `toSummaryResponse` și `toWithCoursesResponse`.

---

### H5. Lipsesc `findByUserEmail` / `existsByUserEmail` din repository
**`professor/repository/ProfessorRepository.java`** (Story 4.1)

Sunt necesare pentru lookup în register (când userType=PROFESOR și emailul există deja).

---

### H6. Lipsește `@Transactional(readOnly = true)` pe Query Service
**`professor/service/ProfessorQueryServiceImpl.java`** (Story 7.2)

**De ce contează:** mapper-ul citește `professor.getUser()` (LAZY) și `professor.getCourses()` (LAZY). Fără tranzacție, ai `LazyInitializationException` la prima cerere reală.

**Cum repari:** `@Transactional(readOnly = true)` pe clasă (sau pe metodă).

---

### H7. Lipsesc complet testele (Story 13)
**`src/test/java/.../professorTest/`** — nu există

Backlog cere minim 5 teste:
- `updateProfesor_ok`
- `updateProfesor_notFound_throws`
- `assignCourse_ok` → verifică `course.getProfessor().getId() == professorId`
- `unassignCourse_ok` → verifică `course.getProfessor() == null`
- `deleteProfesor_cascadesCourseToNull` → cursul rămâne, profesorul lipsește

Plus `ProfessorControllerTest` cu `@WebMvcTest` care verifică 403 fără permisiune și 200 cu permisiune.

---

## 🟡 MEDIUM (bug-uri tăcute / probleme JPA)

### M1. `@OneToMany` fără cascadă rupe `deleteProfesor`
**`professor/model/Professor.java:45`**

```java
@OneToMany(mappedBy = "professor", fetch = FetchType.LAZY)
private Set<Course> courses = new HashSet<>();
```

La `deleteProfessor(id)`, cursurile încă au FK către professor → fie eroare FK constraint, fie cursurile nu sunt actualizate. Testul `deleteProfesor_cascadesCourseToNull` nu va trece.

**Cum repari:** în `deleteProfessor` (service), iterează prin `professor.getCourses()` și setează `course.setProfessor(null)` înainte de delete. SAU adaugă `@PreRemove` pe Professor.

---

### M2. `equals`/`hashCode` includ relații lazy (cycle risk)
**`professor/model/Professor.java:50-59`**

`equals` și `hashCode` includ `user`. Dacă pui Professor într-un `Set`, Hibernate trebuie să încarce User → poate triggera lazy loading în context greșit. Best practice: equals/hashCode doar pe `id` (după ce e setat) sau pe câmp natural unic.

---

### M3. `AuthServiceImpl` nu validează `yearExperience`
**`auth/authService/AuthServiceImpl.java`** (verificare câmpuri profesor)

Validează doar `specialty == null || departament == null`. Lipsește verificarea `yearExperience` (poate veni `null` dacă DTO-ul îl ține `Integer`, sau `0` dacă e primitiv → trece tăcut).

---

### M4. `@OneToOne` pe Professor.user fără strategie de fetch
**`professor/model/Professor.java:42`**

`@OneToOne private User user;` — default fetch este EAGER pentru OneToOne. Asta înseamnă că orice query pe Professor încarcă și User. Bine pentru DTO mapping, dar pentru liste mari trage N+1 dacă mapezi `user.getFirstName()`.

**Sugestie:** lasă EAGER (mapper-ul va vrea User-ul oricum), dar conștientizează.

---

## 🔵 LOW / NIT (polish)

### N1. Typo în numele fișierului
`ProfessorControler.java` → `ProfessorController.java` (cu 2× `l`). Inconsistent cu `StudentController`, `CourseController`.

### N2. Câmp public pe entitate
**`Professor.java:34`** — `public String specialty;` (toate celelalte sunt private). Lombok `@Getter`/`@Setter` deja generează acces; `public` strică encapsularea.

### N3. `@NotNull` pe `int` primitiv
**`Professor.java:39-40`** — `@NotNull private int yearExperience;`. Primitivul nu poate fi null. Sau folosești `Integer` (și păstrezi `@NotNull`), sau scoți `@NotNull` și pui `@Min(0)`.

### N4. `@Component` în loc de `@Service`
**`ProfessorCommandServiceImpl.java:21`, `ProfessorQueryServiceImpl.java`** — convenția Spring e `@Service` pe layer-ul de service.

### N5. `ProfessorRequest` fără `@Min(0)` pe `yearExperience`
**`professor/dtos/ProfessorRequest.java`** — backlog Story 3.1 cere `@Min(0) experientaAni`.

### N6. Commit messages
Toate commit-urile recente sunt `commit` / `next task` / `push`. Greu de revizuit istoricul. Convenția repo-ului (vezi `feat fix`, `fix:studentId`) e oarecum convențională — încearcă `feat:`, `fix:`, `refactor:` cu descriere scurtă.

---

## ✅ Ce ai făcut bine

- **Relații JPA corecte ca structură** — `ManyToOne` Course→Professor și `OneToMany` Professor→Course oglindite corect cu `mappedBy`.
- **Excepții integrate clean** — `ProfessorNotFoundException` (404) + `ProfessorAlreadyExistsException` (409) înregistrate corect în `GlobalExceptionsHandler`.
- **Auth integration** — `permissionsForType(PROFESOR)` în `AuthServiceImpl` are setul corect de permisiuni; register creează User+Professor în aceeași tranzacție.
- **Story 12 implementat complet** — `findByCourseProfessorId` în `EnrolmentRepository` + endpoint `GET /{professorId}/students` cu permisiune corectă (`STUDENT_VIEW`).
- **Pattern Command/Query** respectat (folder split, interfețe separate). Bun.
- **`assignCourse` / `unassignCourse`** — actualizezi ambele părți ale relației (course owner + collection în memory). Corect.

---

## 📋 Story-uri lipsă (checklist)

- [ ] Story 1.2 — `Professor` cu `@JoinColumn(name="user_id", unique=true, nullable=false)`, fără firstName/lastName duplicate
- [ ] Story 3.2 — `ProfessorPatchRequest` cu toate câmpurile opționale
- [ ] Story 3.4 — `ProfessorWithCoursesResponse`
- [ ] Story 4.1 — `findByUserEmail` + `existsByUserEmail`
- [ ] Story 6.1 — `toSummaryResponse()` + `toWithCoursesResponse()` în mapper
- [ ] Story 7.2 — `@Transactional(readOnly = true)` pe QueryServiceImpl
- [ ] Story 8.1 — `addProfessor` să arunce `UnsupportedOperationException`
- [ ] Story 8.2 — `updateProfessor` să actualizeze `yearExperience`; `patchProfessor` să fie cu null-check pe fiecare câmp
- [ ] Story 9.2 — `GET /{id}` endpoint + corectează permisiunea pe `GET /`
- [ ] Story 13.1 — `ProfessorCommandServiceImplTest`
- [ ] Story 13.2 — `ProfessorControllerTest` cu @WebMvcTest

Story-uri OK: **2, 5, 10, 11, 12**.

---

## 🛠 Ordine recomandată de rezolvare

1. **C4, C3, C2** (controller fixes — 15 min)
2. **C1, C5** (PATCH bug + addProfessor → UOE — 20 min)
3. **C6 + M1** (refactor Professor entity — scoate firstName/lastName, fix cascada delete — 45 min) → important să faci `mvn clean compile` și să rulezi register manual din Postman după
4. **H1, H2, H3, H4, H5, H6** (umplere goluri backlog — ~1h)
5. **H7 + Story 13** (teste — ~1h)
6. **N1-N6** (polish — 15 min)

---

## 💬 Comentariu final

Ai prins bine partea grea: relațiile JPA, transacțiile, integrarea cu auth-ul existent. Punctele slabe sunt **detaliile de contract** (paths, permisiuni, `@Valid`, `@Transactional(readOnly)`) și **respectarea backlog-ului ca specificație** — multe lucruri din backlog sunt explicit cerute (ex: `addProfessor` să arunce UOE), nu sugestii.

Recomandare pentru următoarea iterație: când implementezi după un backlog, deschide-l pe ecran și bifează fiecare punct pe măsură ce-l rezolvi. Apoi rulezi din Postman fiecare endpoint din tabela Story 9.2 înainte să zici „gata".
