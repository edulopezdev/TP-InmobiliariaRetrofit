🏠 Requerimientos - Inmobiliaria 2025

Este documento contiene la transcripción completa del PDF Inmobiliaria_2025.pdf, organizada y limpia para su uso con GitHub Copilot o cualquier asistente de código dentro de Android Studio.

📘 Descripción general

El sistema trata sobre la informatización de la gestión de alquileres de propiedades inmuebles que realiza una agencia inmobiliaria.

👥 Relaciones principales

En la inmobiliaria se alquilan inmuebles para distintos usos: departamentos, locales, depósitos, oficinas, etc.

Los propietarios ofrecen sus inmuebles a la agencia, que busca inquilinos y gestiona los contratos de alquiler por tiempo determinado.

El propietario debe aceptar al inquilino propuesto por la agencia si cumple las condiciones estipuladas.

Un propietario puede ser dueño de uno o varios inmuebles. Cada inmueble pertenece a un único propietario.

Un inquilino puede tener varios contratos, pero es responsable individualmente de cada uno.

Cada contrato de alquiler está asociado a un solo inmueble.

A lo largo del tiempo, un inmueble puede aparecer en varios contratos de alquiler no vigentes.

Cada contrato tiene pagos asociados con número de pago, fecha e importe.

🏠 Proceso: Propietario entrega inmueble

Cuando un propietario entrega un inmueble, la agencia solicita:

Dirección

Uso (comercial o residencial)

Tipo (local, depósito, casa, departamento, etc.)

Cantidad de ambientes

Precio del inmueble

El estado inicial del inmueble será disponible.

Si el propietario no estaba registrado:

Se debe ingresar su DNI, apellido, nombre y datos de contacto.

Queda a la espera de que la agencia lo contacte cuando haya un contrato con un inquilino.

👤 Proceso: Inquilino alquila inmueble

El inquilino se entrevista y se registran sus datos personales:

DNI

Nombre completo

Lugar de trabajo

Nombre y DNI del garante

Datos de contacto del inquilino y del garante

El inquilino indica las características del inmueble que busca:

Uso (local, casa, etc.)

Tipo

Ambientes

Precio aproximado

El sistema busca inmuebles disponibles.

Si hay coincidencias, se entrega una lista de opciones.

Si el inquilino elige uno, se marca como no disponible y se crea el contrato de alquiler.

Si no hay coincidencias, se muestra un mensaje informando que no hay inmuebles disponibles.

Al crear el contrato se registran:

Fecha de inicio

Fecha de finalización (validar coherencia de fechas)

Monto de alquiler (en pesos)

Vínculo entre propiedad e inquilino

Luego se avisa al propietario para la firma del contrato.

🧾 Terminación y renovación de contratos

El inquilino puede terminar el contrato antes de tiempo, pagando una multa.

Si rescinde antes de la mitad del período de alquiler → paga 2 meses extra.

Si rescinde después de la mitad → paga 1 mes extra.

Se debe actualizar la fecha de fin del contrato y calcular la multa, aunque el sistema no registre el pago de la misma.

Se debe verificar que no haya deudas de alquiler pendientes.

El sistema debe permitir renovar fácilmente un contrato de alquiler (nuevo monto y fechas, mismos inquilino e inmueble).

📋 Listados requeridos

El sistema debe poder generar los siguientes listados:

Propiedades disponibles y su dueño.

Todas las propiedades de un propietario determinado.

Todos los contratos vigentes (por fechas).

Dado un inmueble, listar todos sus contratos y el nombre del inquilino.

Listar los pagos realizados para un alquiler en particular.

💰 Proceso: Inquilino paga alquiler

Registrar:

Número de pago

Fecha del pago

Importe

Se debe poder listar todos los pagos asociados a un contrato.

📱 Aplicación móvil

La inmobiliaria dispone de una app móvil para propietarios, que podrán acceder previo registro.

✅ Reglas de negocio resumidas

Un inmueble pertenece a un único propietario.

Un contrato está asociado a un solo inmueble y un solo inquilino.

Los pagos pertenecen a un único contrato.

Los inmuebles tienen estado: disponible / no disponible.

Las fechas de inicio y fin deben ser válidas (la de inicio no puede ser posterior a la de fin).

Las multas se calculan según la duración cumplida del contrato.

El sistema debe permitir renovar contratos y registrar pagos fácilmente.

🧩 Recomendaciones para implementación

Crear clases o entidades para:

Propietario

Inquilino

Inmueble

Contrato

Pago

Relaciones sugeridas:

Propietario → Inmueble (1 a muchos)

Inquilino → Contrato (1 a muchos)

Inmueble → Contrato (1 a muchos, pero solo uno vigente)

Contrato → Pago (1 a muchos)