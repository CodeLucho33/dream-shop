
# Dream Shop

**Dream Shop** es una aplicación de comercio electrónico desarrollada con Spring Boot. El objetivo de este proyecto es crear una plataforma simple y eficiente para gestionar productos, categorías e imágenes.

## Tecnologías utilizadas
- **Java 21**
- **Spring Boot 3.4.3**
- **Spring Data JPA**
- **MySQL**
- **Lombok**

## Funcionalidades
- Gestión de productos (nombre, marca, precio, inventario, descripción).
- Organización de productos por categorías.
- Almacenamiento y visualización de imágenes de productos.

## Instalación y configuración
1. Clona el repositorio:
```bash
git clone <URL_DEL_REPOSITORIO>
```
2. Configura la base de datos MySQL en `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/dream_shop
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA
spring.jpa.hibernate.ddl-auto=update
```
3. Ejecuta la aplicación:
```bash
./mvnw spring-boot:run
```

## Modelos principales
### Product
- id (Long)
- name (String)
- brand (String)
- price (BigDecimal)
- inventory (int)
- description (String)
- category (Category)
- images (List<Image>)

### Image
- id (Long)
- fileName (String)
- fileType (String)
- blob (Blob)
- downloadUrl (String)
- product (Product)

### Category
- id (Long)
- name (String)
- products (List<Product>)  
