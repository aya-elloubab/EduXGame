# EduGame: Gamified Learning Platform for Secondary Education in Morocco

<div align="center"
  <img src="https://github-production-user-asset-6210df.s3.amazonaws.com/152920860/399134797-ffacb129-d7aa-4b10-8103-3982691f3b71.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAVCODYLSA53PQK4ZA%2F20241229%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20241229T122222Z&X-Amz-Expires=300&X-Amz-Signature=0e65b685c6b7900f281efc203a678e1d16522e1b0a3bd555b2570daf4b9c0542&X-Amz-SignedHeaders=host" width="300" alt="EduGame Logo">
</div>
EduGame is a gamified micro-learning platform aimed at enhancing student engagement and retention through interactive lessons, quizzes, and games. Designed for secondary-level education in Morocco, the platform combines technology and gamification to deliver a unique educational experience.

---

## Table of Contents

- [Software Architecture](#software-architecture)
- [Docker Configuration](#docker-configuration)
- [Frontend](#frontend)
- [Backend](#backend)
- [Getting Started](#getting-started)
- [Features](#features)
- [Contributing](#contributing)
- [Future Enhancements](#future-enhancements)

---

## Software Architecture

![EduGame Architecture]![WhatsApp Image 2024-12-29 at 03 31 51_40bfa45c](https://github.com/user-attachments/assets/c9bbde5b-7904-4613-9fe2-424f01733194)


The EduGame platform consists of the following components:

- Frontend: Developed using Next.js and Tailwind CSS for responsive and dynamic UI.
- Backend: Built with Spring Boot and Flask, offering APIs for data management and file interactions.
- Database: MySQL is used for persistent data storage.
- Mobile Support: Android client developed with Java, offering native functionality.
- APIs: RESTful APIs enable communication between services.

---

## Docker Configuration

The system is containerized for consistent deployment. Below is an example of the docker-compose.yml configuration:

yaml
version: "3.8"

services:
  db:
    image: mysql:8.0
    container_name: edugame_db
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: edugame_db
    networks:
      - edugame-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5

  spring-backend:
    build:
      context: ./backend-spring
    ports:
      - "8088:8088"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/edugame_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
      - SPRING_DATASOURCE_USERNAME=root
      - SPRING_DATASOURCE_PASSWORD=rootpassword
    depends_on:
      db:
        condition: service_healthy
    networks:
      - edugame-network

  flask-backend:
    build:
      context: ./backend-flask
    ports:
      - "8080:8080"
    environment:
      - OPENAI_API_KEY=${OPENAI_API_KEY}
    volumes:
      - ./backend-flask:/app
    depends_on:
      - spring-backend
    networks:
      - edugame-network

  nextjs-frontend:
    build:
      context: ./frontend-next
    ports:
      - "3000:3000"
    depends_on:
      - db

networks:
  edugame-network:
    driver: bridge



---

## Frontend

### Technologies Used

- **Next.js**: Modern React framework for building interactive web applications.
- **Tailwind CSS**: Utility-first CSS framework for styling.
- **TypeScript**: Ensures code reliability and maintainability.
- **XML**: Used for defining Android UI layouts.
- **Lottie**: Adds engaging animations to the interface.

### Highlights

- Dynamic dashboards for managing quizzes, leaderboards, and rewards.
- Real-time feedback to track user progress and achievements.

---

## Backend

### Technologies Used

- **Spring Boot**: Manages core business logic and API endpoints.
- **Flask**: Processes PDF uploads with LangChain for advanced content extraction.
- **MySQL**: Stores user data, quiz results, and progress.

### Key Features

1. **Authentication and Security**:
   - JWT-based authentication for secure access.

2. **API Endpoints**:
   - RESTful APIs for managing levels, courses, chapters, and user progress.

---

## Getting Started

### Prerequisites

1. Install [Docker](https://www.docker.com/).
2. Install Node.js (version 16 or later).
3. Install Java (JDK 17).

### Steps

1. Clone the repository:
   bash
   git clone https://github.com/aya-elloubab/EduGame.git
   cd EduGame
   

2. Build and run the backend:
   bash
   cd spring-boot
   ./mvnw spring-boot:run
   

3. Start the frontend:
   bash
   cd frontend
   npm install
   npm run dev
   

4. Access the app at http://localhost:3000.
5. Start android app:
     build and run app on device

---

## Features

- **Gamified Quizzes**: Engage students with interactive and fun quizzes.
- **Adaptive Learning**: Personalized content tailored to individual progress.
- **Leaderboards**: Competitive rankings to motivate and encourage participation.
- **Flashcards**: Simplify learning with key concept cards for quick review.
- **Progress Tracking**: Real-time insights into student achievements and milestones.
- **PDF Content Extraction**: Automatically generate educational content from uploaded PDFs.


---
### Example: Chapter Creation
1. Navigate to the admin panel.
2. Upload a PDF containing course material.
3. Review and edit the extracted content (quizzes, flip cards, etc.) in the chapter management page.
4. Save the chapter for learners to access.
---
## Contributing

Contributions are welcome! Please fork this repository and create a pull request.

### Contributors

- Elloubab Aya ([GitHub](https://github.com/aya-elloubab))
- Ait Ouahda Younes ([GitHub](https://github.com/YounesAO))

---

## Future Enhancements

1. AI Recommendations: Personalized suggestions based on performance.
2. Offline Mode: Enable app access without internet.
3. Multi-language Support: Extend the platform to other regions.

---

## License

This project is licensed under the MIT License. See the LICENSE file for more details.
