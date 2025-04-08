# Gym Management System

## Integrantes del equipo de desarrollo
Enrique Escobar, e.escobar.2022@alumnos.urjc.es, EnriqueEscobar-URJC

## Aspectos Principales
### Entidades
- **User**: Representa a los usuarios del sistema, quienes pueden registrarse, iniciar sesión, y gestionar su información personal.
- **Review**: Permite a los usuarios dejar opiniones sobre el gimnasio o sus servicios.
- **Gimclass**: Representa las clases de gimnasio, incluyendo detalles como nombre, descripción, horario, duración y usuarios inscritos.

### Permisos de Usuarios
- **User**: Puede registrarse, iniciar sesión, inscribirse en clases, y dejar reseñas.
- **Anónimos**: Pueden navegar por la web, pero no pueden realizar acciones hasta que se registren.

### Imágenes
Las entidades con soporte para imágenes son:
- **User**: Foto de perfil. Limitado a imágenes de 300x300 y formato JPEG.

## Desarrollo Individual
### Enrique Escobar
Como único desarrollador del proyecto, me he encargado de implementar todos los aspectos del sistema, incluyendo:
- **User**: Gestión de usuarios, registro, inicio de sesión, y edición de información personal.
- **Review**: Creación, edición y eliminación de reseñas, así como su visualización.
- **Gimclass**: Gestión de clases de gimnasio, incluyendo creación, edición, eliminación, y la funcionalidad para que los usuarios se inscriban o se desinscriban de las clases.

## Tecnologías Utilizadas
- **Backend**: Java con Spring Boot.
- **Frontend**: HTML, CSS, y JavaScript.
- **Base de Datos**: MySQL para el almacenamiento de datos.

## API REST y DTOs
Se han implementado API REST para permitir la interacción entre el frontend y el backend de manera eficiente. Además, se han utilizado DTOs (Data Transfer Objects) para estructurar y simplificar la transferencia de datos entre las capas de la aplicación.

## Funcionalidades Principales
1. **Gestión de Usuarios**:
    - Registro, inicio de sesión y edición de información personal.
    - Subida de imágenes de perfil.
2. **Gestión de Clases de Gimnasio**:
    - Creación, edición y eliminación de clases.
    - Inscripción y desinscripción de usuarios en clases.
3. **Sistema de Reseñas**:
    - Creación, edición y eliminación de reseñas por parte de los usuarios.
    - Visualización de todas las reseñas.

## Próximos Pasos
- Realizar paginación mediante AJAX de una de las entidades
- Realizar una consulta dinámica sobre una de las entidades

## Funcionalidades No Implementadas
- **Memberships**: Aunque estaba planeado incluir un sistema de membresías para gestionar los planes de suscripción de los usuarios, esta funcionalidad no se ha implementado. Sin embargo, he acordado con mi profesor que, dado que soy el único desarrollador del proyecto, no era necesario incluir esta característica.

