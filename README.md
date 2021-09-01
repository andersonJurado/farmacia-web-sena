# Pharmacy

## Desarrollo

Despues de clonar el proyecto se debe instalar las siguientes dependencias en tu computadora:

1. [Node.js] https://nodejs.org/es/download/
Despues de instalar Node se debe utilizar la utilidad CLI para instalar las librerias necesarias\

```
npm install
```

Los siguientes comandos se escriben en terminales separadas uno para correr el servidor y la base de datos esto se hace con maven
para ello es necesario tener previamente instalada una version de Java.

## Con respecto a las tecnologias del lado del servidor

se utiliza spring boot , spring security, spring mvc REST + Jackson, Spring Data JPA, como manejador de dependencias utilizamos Maven,
la base de datos utilizada es mysql para produccion y desarrollo. Usamos el siguiente comando para correr el backend
```
./mvnw
```
## Con respecto a las tecnologias del lado del cliente

El frontend esta desarrollado con el framework vue2, el framework de css Bootsrap junto con el preprocesador Sass ademas tiene compatibilidad con internacionalizacion.
Porsupuesto todo esto engloba la base de HTML5, Css y Javascript. Usamos el siguiente comando npm para correr el frontend
```
npm start
```


## Nota: la ruta del proyecto para acceder a el contenido base del Layout(html5,css(boostrap,sass),javascrip(vue)) es
## src/main/webapp/index.html

## Esta plataforma esta hecha con el fin de dar soporte a la farmacia san sebastian en el municipio de sibundoy.
