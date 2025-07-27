# 🧾 Vendor Management System

A full-stack vendor and invoice management system built with **React** (frontend), **Spring Boot** (backend), and **PostgreSQL** for persistence. Invoice documents are stored securely using **AWS S3**.

## ✨ Features

- 📇 Add and manage vendors with contact information
 - 📤 Export invoices to Excel/CSV
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

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register` | User registration |
| POST | `/auth/login` | User login (returns JWT token) |

### Vendors
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/vendors` | List all vendors |
| GET | `/api/vendors/{id}` | Get vendor by ID |
| POST | `/api/vendors` | Create new vendor |
| PUT | `/api/vendors/{id}` | Update vendor |
| DELETE | `/api/vendors/{id}` | Delete vendor |
| GET | `/api/vendors/vendor-services` | Get vendors with services |
| GET | `/api/vendors/test` | System health check |

### Vendor Services
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/vendor-services` | Create vendor service |
| GET | `/api/vendor-services/{id}` | Get vendor service by ID |
| GET | `/api/vendor-services/vendor/{vendorId}` | Get services by vendor |
| PUT | `/api/vendor-services/{id}` | Update vendor service |
| DELETE | `/api/vendor-services/{id}` | Delete vendor service |

### Service Types
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/service-types` | List all service types |
| GET | `/api/service-types/{id}` | Get service type by ID |
| POST | `/api/service-types` | Create service type |
| PUT | `/api/service-types/{id}` | Update service type |
| DELETE | `/api/service-types/{id}` | Delete service type |

### Invoices
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/invoices` | System message |
| GET | `/api/invoices/by-vendor-service/{vendorServiceId}` | Get invoices by vendor service |
| POST | `/api/invoices` | Create invoice with file upload |
| GET | `/api/invoices/{id}/download` | Download invoice bill |
| GET | `/api/invoices/{id}/preview` | Preview invoice (presigned URL) |
| DELETE | `/api/invoices/{id}/delete` | Delete invoice bill from S3 |
| DELETE | `/api/invoices/{invoiceId}` | Delete invoice record |
| GET | `/api/invoices/by-vendor-service/{vendorServiceId}/created-at` | Get invoices by date range |

### Excel/CSV Export
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/excel/invoices/download` | Download invoices as Excel |
| GET | `/api/excel/invoices/download-csv` | Download invoices as CSV |
| GET | `/api/excel/invoices/download/created-at` | Download Excel by creation date |
| GET | `/api/excel/invoices/download-csv/created-at` | Download CSV by creation date |

## 📁 Project Structure

```
Vendor_Management/
├── backend/
│   ├── .mvn/
│   ├── src/main/java/com/vendormanagement/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   └── model/
│   ├── pom.xml
│   ├── mvnw
│   └── mvnw.cmd
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   └── services/
│   ├── package.json
│   └── package-lock.json
├── .gitignore
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
