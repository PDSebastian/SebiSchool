# Următorii pași — sebi-school

Lista de fix-uri și îmbunătățiri ordonate după impact. Începe de sus în jos.

---

## 1. Fix HTTP status codes în `GlobalExceptionsHandler` 🔴

**Fișier:** `src/main/java/ro/mycode/sebischool/system/exceptions/GlobalExceptionsHandler.java`

`handleBadRequest` (linia 42-50) prinde toate `*NotFoundException` și returnează **HTTP 409 CONFLICT** cu body `status: 400`. Greșit pe ambele.

**Ce trebuie:**
- `StudentNotFoundException`, `BookNotFoundException`, `CourseNotFoundException`, `EnrolmentNotFoundException`, `UserNotFoundException` → **HTTP 404 NOT_FOUND**
- Sincronizează `apiErrorResponse.status` cu status-ul HTTP returnat (ambele 404)

**De ce contează:** clientul (browser/Postman/frontend) nu poate diferenția "nu există" de "deja există" — ambele întorc 409.

---

## 2. Implementează `updateEnrolment` corect 🔴

**Fișier:** `src/main/java/ro/mycode/sebischool/enrolment/service/commanService/EnrolmentCommandServiceImpl.java` (linia 56-61)

Acum face `findById` + `save` fără să copieze nimic din `request`. PUT-ul nu face nimic.

**Ce trebuie:** dacă `EnrolmentRequest` are `studentId` / `courseId`, re-leagă entitățile (vezi `addEnrolment` ca model — caută student, caută course, set pe enrolment).

---

## 3. Scoate dublu-save în `updatePatchBook` 🔴

**Fișier:** `src/main/java/ro/mycode/sebischool/books/service/commandService/BookCommandServiceImpl.java` (linia 65-66)

```java
bookRepository.save(b);                          // ← șterge linia asta
return BookMapper.toDto(bookRepository.save(b)); // ← păstrează doar asta
```

`save` o singură dată. Returnează rezultatul direct.

---

## 4. Validare corectă pentru `age` 🟡

**Fișier:** `src/main/java/ro/mycode/sebischool/student/service/commandService/StudentCommandServiceImpl.java` (linia 39)

Acum validezi doar `age > 100`. Un student cu `age = -5` trece.

**Ce trebuie:**
```java
if (studentRequest.getAge() < 0 || studentRequest.getAge() > 100) {
    throw new InvalidStudentAgeException();
}
```
Aplică aceeași validare și în `updatePatchStudent` când `age != null`.

---

## 5. `deleteEnrolment` — alege un contract și ține-l 🟡

**Fișier:** `EnrolmentCommandServiceImpl.java` (linia 65-72) + `EnrolmentController.java` (linia 34-39)

Acum service-ul returnează `null` dar controller-ul declară `ResponseEntity<EnrolmentResponse>` cu status 200. Inconsistent cu `StudentController.deleteStudent` care întoarce `noContent()` (204).

**Ce trebuie:** uniformizează — schimbă `EnrolmentController.deleteEnrolment` să întoarcă `ResponseEntity<Void>` cu `noContent()`, și `deleteEnrolment` în service să fie `void`.

---

## 6. Adaugă `@PreAuthorize` pe controllere 🔴 (Security)

Ai definit permissions detaliate (`COURSE_VIEW`, `COURSE_MANAGE`, `ENROL_SELF`, `USER_EDIT`...) dar **nici un controller nu le folosește**. Orice user autentificat poate face orice — inclusiv un STUDENT poate șterge cursuri.

**Pași:**
1. În `SecurityConfiguration` adaugă `@EnableMethodSecurity` pe clasa de configurare.
2. Pe controllere, pune adnotări:
   - `CourseController.addCourse / updateCourse / deleteCourse / patchCourse` → `@PreAuthorize("hasAuthority('COURSE_MANAGE')")`
   - `CourseController.getAll* / getCourseBy*` → `@PreAuthorize("hasAuthority('COURSE_VIEW')")`
   - `EnrolmentController.addEnrolment` → `@PreAuthorize("hasAuthority('ENROL_SELF') or hasAuthority('COURSE_MANAGE')")`
   - `StudentController.deleteStudent` → `@PreAuthorize("hasAuthority('USER_DELETE')")` (vezi pct. 7)

**Testare:** loghează-te ca STUDENT, încearcă POST /courses → ar trebui 403.

---

## 7. Rename `USER_DELELTE` → `USER_DELETE` 🟡

**Fișier:** `src/main/java/ro/mycode/sebischool/users/security/UserPermissions.java` (linia 9) + `AuthServiceImpl.java` (linia 118)

Typo: "DELELTE" în loc de "DELETE". Apare așa și în token-ul JWT și în tabelul `user_permissions` din DB.

**Pași:**
1. Rename în enum: `USER_DELELTE("USER_DELELTE")` → `USER_DELETE("USER_DELETE")`.
2. Rename în `AuthServiceImpl.permissionsForType`.
3. **Migrare DB** pentru userii existenți (sau șterge tabelul `user_permissions` și relogheză-te — ești în dev).

---

## 8. Rename `Userrepository` → `UserRepository` 🟢

Convenție Java standard: clasele cu PascalCase. Acum e `Userrepository` (litera mică la `r`).

IntelliJ: Shift+F6 pe nume clasă → redenumire automată în tot proiectul.

---

## 9. Șterge cod mort: `EnrolmentMapper.toEntity` 🟢

**Fișier:** `src/main/java/ro/mycode/sebischool/enrolment/mappers/EnrolmentMapper.java` (linia 10-16)

Returnează `Enrolment.builder().build()` (gol). Nu e apelat nicăieri. Șterge metoda — Command service-ul construiește direct.

---

## 10. Comentariu pe JWT secret default 🟢

**Fișier:** `src/main/resources/application.yaml` (linia 27)

Fallback-ul `asdasdasdasdasd...` e ok pentru dev local, dar **în prod** trebuie `JWT_SECRET_KEY` setat din env var. Adaugă un comentariu deasupra:
```yaml
# JWT_SECRET_KEY trebuie obligatoriu setat in prod (env var).
# Default-ul de mai jos e DOAR pentru dev local.
secretKey: ${JWT_SECRET_KEY:asdasdasdasdasd...}
```

---

## Cum verifici că ai terminat

Pentru fiecare punct de mai sus:

```bash
# 1. Compilează
./mvnw clean compile

# 2. Rulează testele existente
./mvnw test

# 3. Pornește app-ul + testează manual ce ai modificat (Postman/curl)
./mvnw spring-boot:run
```

Pentru punctul 6 (PreAuthorize), testează cu **2 useri diferiți** (unul STUDENT, unul PROFESOR) ca să vezi că restricțiile funcționează.

---

## Ordine recomandată

1. **Întâi 1-5** (fix-uri de corectitudine în cod existent — schimbări mici, fiecare commit separat)
2. **Apoi 6** (security — testează cu grijă, e ușor să blochezi useri din greșeală)
3. **La urmă 7-10** (cleanup / convenții — mecanic, puțin risc)
