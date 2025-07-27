# 🧾 Vendor Management System

A full-stack vendor and invoice management system built with **React** (frontend), **Spring Boot** (backend), and **PostgreSQL** for persistence. Invoice documents are stored securely using **AWS S3**.

## ✨ Features

- 📇 Add and manage vendors with contact information
- 🧾 Create and track invoices per vendor
- 📁 Upload and view invoice files (PDFs/Images)
- 🔐 Role-based access control
- 🖥️ Responsive and modern React frontend
- ☁️ AWS S3 integration for file storage

## 🎥 Demo

📺 [Watch the Complete Demo Video](https://drive.google.com/file/d/13TgXO_-mJ-c0pVNAoyepE6WELNqucYQo/view?usp=sharing)

## ⚙️ Tech Stack

| Layer       | Technology         |
|-------------|--------------------|
| Frontend    | React, Axios       |
| Backend     | Spring Boot (Java) |
| Database    | PostgreSQL         |
| Storage     | AWS S3             |
| Tools       | Maven, Git         |

## 🛠️ Prerequisites

- Java 17+
- Node.js 16+ and npm
- Maven 3.6+
- PostgreSQL
- AWS S3 bucket + access keys

## 🔧 Environment Variables

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/vendor_db
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password

AWS_REGION=your-region
AWS_ACCESS_KEY=your-access-key
AWS_SECRET_KEY=your-secret-key
AWS_S3_BUCKET=your-s3-bucket-name
```

## 🚀 Getting Started

### Backend Setup

```bash
cd backend
mvn clean install
mvn spring-boot:run
```
Backend runs at: `http://localhost:8080`

### Frontend Setup

```bash
cd frontend
npm install
npm start
```
Frontend runs at: `http://localhost:3000`

## 🧪 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/vendors` | List all vendors |
| POST | `/api/vendors` | Create new vendor |
| GET | `/api/invoices` | List all invoices |
| POST | `/api/invoices` | Create new invoice |
| POST | `/api/invoices/{id}/upload` | Upload invoice document |

## 📁 Project Structure

```
vendor-management-system/
│   ├── src/main/java/com/vendormanagement/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   └── model/
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   └── services/
│   └── package.json
└── README.md
```

## 🧠 System Design

📊 **ER Diagram** *(coming soon)*  
🏗️ **System Architecture** *(coming soon)*

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit changes (`git commit -m 'Add new feature'`)
4. Push to branch (`git push origin feature/new-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License.

---
