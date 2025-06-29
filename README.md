# 🛠️ Sanay3y – Scalable Microservices Platform for Home Services

Sanay3y is a scalable, microservices-based application that connects users with trusted home service professionals — including electricians, plumbers, carpenters, and more. Built with flexibility and performance in mind, this project solves the inefficiencies of informal word-of-mouth recommendations by providing a verified, location-aware booking experience.

---

## 🚀 Key Features

- 🔎 **Smart Search**: Filter workers by location, availability, category, and rating.
- 👤 **User Dashboard**: View booking history, favorite workers, and manage profile.
- 👷 **Worker Tools**: Set working hours, manage job orders, and update availability.
- 💬 **Review System**: Submit reviews, vote helpful ones, and view aggregate ratings.
- 📅 **Booking Flow**: Schedule, cancel, reschedule appointments with live worker status.
- 🔁 **Real-time Updates**: Reflected across services using async message queues.
- 📦 **Fully Containerized**: Deployed using Docker & orchestrated via Kubernetes.

---

## 🧱 Microservices Architecture

Each service is designed with separation of concerns, its own database (SQL or NoSQL), and appropriate design patterns.

| Service | Description | DB | Patterns Used |
|--------|-------------|----|----------------|
| **User Service** | Auth, profile, favorites, booking history | PostgreSQL | Singleton, Command |
| **Worker Service** | Manage skills, availability, login | MongoDB | Factory, Decorator |
| **Search Service** | Filter by category, location, availability | Redis Cache | Strategy, Factory |
| **Review Service** | Add reviews, compute ratings | MongoDB | Observer, Builder |
| **Booking Service** | Full booking lifecycle + state changes | PostgreSQL | Command, Observer |

---

## ⚙️ Tech Stack

- **Backend**: Spring Boot (Java)
- **Databases**: PostgreSQL, MongoDB, Redis
- **Messaging**: RabbitMQ (for async communication)
- **Containerization**: Docker
- **Orchestration**: Kubernetes (load balancing, service discovery)
- **CI/CD**: GitHub Actions
- **Monitoring**: Grafana
- **Logging**: Loki / Manual logs

---

## 🔁 Communication Strategy

### 🔄 Synchronous (REST APIs)
- User ↔ Review (fetch/display user reviews)
- Worker ↔ Search (real-time availability)
- Review ↔ Worker (attach reviews to profiles)
- Booking ↔ Worker (slot availability validation)

### 📩 Asynchronous (RabbitMQ)
- Booking → Worker (update availability)
- Review → Worker (update average rating)
- User → Review (soft delete user reviews)
- Worker → Search (real-time cache updates)

---

## 💡 Design Highlights

- **Scalable Caching**: Search results are cached in Redis for fast response.
- **Dynamic Worker Types**: Using Factory pattern for different worker professions.
- **Flexible Reviews**: Built with optional fields, tags, and anonymous posting.
- **Pluggable Commands**: Booking actions use Reflection for dynamic execution.

---

## 🧪 Team Contributions

> Developed by a team of 12 members organized into 4 sub-scrums:
- **User Service & Infrastructure**: Aml, Shahenda, Rawan
- **Worker Service & Messaging**: Sama, Rotana, Farah
- **Review System & Gateway**: Yasmin, Rowayda, Salma
- **Search & Booking Services**: Sara, Rana, Abdulrahman

---

## 📌 Future Improvements

- Add Admin Panel for managing workers.
- Implement real-time notifications.
- Expand to support regional dialect search (Arabic NLP).
- Integrate Payment Gateway.

---

## 📬 Contact

For inquiries or collaboration:

- **Shahenda Maisara** – shahenda.elsayed@student.guc.edu.eg
- **Aml Mohamed** – aml.mohamed@student.guc.edu.eg  
- **Sama Samy** – sama.abdelaal@student.guc.edu.eg
- **Yasmin Ahmed** – yasmin.elbdawy@student.guc.edu.eg
- **Sarah Salem** – sara.fouda@student.guc.edu.eg
- **Farah Amr** – farah.abdalla@student.guc.edu.eg
- **Salma Moataz** – salma.abdalhafez@student.guc.edu.eg
- **Rawan Ehab** – rawan.kamel@student.guc.edu.eg
- **Rotana Ahmed** – rotana.abdelrahman@student.guc.edu.eg
- **Rowayda Khaled** – rowayda.owais@student.guc.edu.eg
- **Rana Sabry** – rana.mahmoudsabry@student.guc.edu.eg
- **Abdulrahman Amr** – abdulrahman.mohamed@student.guc.edu.eg

---

⭐ *If you find this project interesting or helpful, please consider starring the repo!*
