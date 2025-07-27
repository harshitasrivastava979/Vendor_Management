# 🧾 Vendor Management System

A full-stack vendor and invoice management system built with **React** (frontend), **Spring Boot** (backend), and **PostgreSQL** for persistence. Invoice documents are stored securely using **AWS S3**.

## ✨ Features

- 📇 Add and manage vendors with contact information
- 🧾 Create and track invoices per vendor
- 📁 Upload and view invoice files (PDFs/Images)
- 🔐 Role-based access control
- 📤 Export invoices to Excel/CSV
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

## 📄 License

This project is licensed under the MIT License.

---
