# SI AMTO - Academic Transcript & AMTO License System

## Deskripsi

SI AMTO adalah sistem backend berbasis Spring Boot yang digunakan untuk mengelola **transkrip akademik mahasiswa** serta **perhitungan nilai AMTO (Aircraft Maintenance Training Organization)** berdasarkan standar lisensi penerbangan.

Sistem ini menghubungkan nilai akademik mahasiswa dengan struktur **AMTO License** seperti **A1, A3, A4, C1, C2, C3, C4** dan menghasilkan **transkrip AMTO** secara otomatis.

---

## Fitur Utama

* Manajemen Data Mahasiswa
* Manajemen Program Studi (Major)
* Manajemen Kurikulum
* Manajemen Mata Kuliah (Subject)
* Manajemen Transkrip Akademik
* Perhitungan Nilai AMTO
* Mapping Subject Akademik ke Subject AMTO
* Transkrip AMTO per License

---

## Struktur Teknologi

Backend menggunakan teknologi berikut:

* Java 17
* Spring Boot
* Spring Data JPA
* Maven
* PostgreSQL / MySQL
* REST API

---

## Struktur Project

src/main/java

```
controller
service
repository
entity
dto
mapper
config
```

src/main/resources

```
application.properties
```

---

## Struktur Database Utama

### Academic System

* student
* major
* curriculum
* curriculum_detail
* subject
* transcript
* transcript_detail

### AMTO System

* license_amto
* major_license
* subject_amto
* subject_amto_detail
* transcript_amto
* transcript_amto_detail

---

## Konsep Perhitungan Nilai AMTO

1 Subject AMTO dapat memiliki beberapa Subject Akademik.

Contoh:

Aircraft System (AMTO)

* Aircraft System 1
* Aircraft System 2
* Aircraft System 3

Nilai AMTO dihitung dengan:

```
Average = (nilai_subject1 + nilai_subject2 + ...)
```

Aturan kelulusan:

* Nilai minimal = **70**
* Jika salah satu nilai < 70 maka subject AMTO dianggap **tidak lulus**

---

## Cara Menjalankan Project

### 1. Clone Repository

```
git clone https://github.com/username/si_amto.git
```

### 2. Masuk ke folder project

```
cd si_amto
```

### 3. Install dependency

```
mvn clean install
```

### 4. Jalankan aplikasi

```
mvn spring-boot:run
```

Aplikasi akan berjalan di:

```
http://localhost:8080
```

---

## API Endpoint (Contoh)

Student

```
POST   /api/students
GET    /api/students
GET    /api/students/{id}
```

Transcript

```
POST   /api/transcripts
GET    /api/transcripts/{studentId}
```

AMTO

```
GET    /api/amto/transcript/{studentId}
```

---

## Kontribusi

Jika ingin berkontribusi:

1 Fork repository
2 Buat branch baru
3 Commit perubahan
4 Buat Pull Request

---

## Author

Project ini dikembangkan untuk sistem akademik dan perhitungan lisensi AMTO pada institusi pendidikan penerbangan.

---


Skema Database
<img width="767" height="533" alt="image" src="https://github.com/user-attachments/assets/90b1a71c-d7c2-465b-a549-ecfc7a50e651" />
