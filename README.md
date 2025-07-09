# Vendor Management System

A JavaFX desktop application for managing vendors and their invoices, integrated with a Spring Boot backend.

## Features

- Add and manage vendors with their contact information
- Create invoices for vendors with detailed information
- Upload invoice files (PDF/Images)
- Automatic local backup of invoice files
- Modern and user-friendly interface

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL database
- Spring Boot backend running on `http://localhost:8080`

## Setup

1. Clone the repository:
```bash
git clone https://github.com/yourusername/vendor-management-system.git
cd vendor-management-system
```

2. Build the application:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn javafx:run
```

## Application Structure

- `src/main/java/com/vendormanagement/`
  - `VendorManagementApplication.java` - Main application class
  - `controller/` - JavaFX controllers
  - `model/` - Data models
  - `service/` - Services for backend communication

- `src/main/resources/`
  - `fxml/` - JavaFX view files
  - `application.properties` - Application configuration

## Usage

1. Start the Spring Boot backend server
2. Launch the JavaFX application
3. Add vendors using the vendor form
4. Click on a vendor to create invoices
5. Fill in invoice details and upload files
6. Invoice files are saved locally at `C:/invoices/{VendorName}/`

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 