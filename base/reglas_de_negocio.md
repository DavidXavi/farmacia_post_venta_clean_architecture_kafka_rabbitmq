Punto de venta (POS) para farmacia.
Se aplican las reglas de cualquier punto de venta:

1. Módulo de Ventas
- Registrar ventas.
- Agregar varios productos.
- Aplicar descuentos.
- Calcular impuestos.
- Registrar diferentes formas de pago.
- Anular ventas.

*Reglas de negocio que se aplican en el módulo de ventas:

a. Reglas de promociones:
Un producto puede tener muchas promociones, por ejemplo:
Si llevas 2 unidades te llevas la 3era unidad gratis.
Si llevas 1 unidad te aplican un 10% de descuento.
Regla 1: Sólo se puede hacer uso de 1 promoción por línea de venta. En caso el producto tenga más de una promoción y cumpla ambas en la venta, el usuario debe indicarle al cliente cuál de las promociones quiere hacer efectiva en su compra)
Regla 2: Todas las promociones sólo pueden aplicarse 1 vez por comprobante.
Regla 3: Existen promociones que exigen que el cliente se registre en el sistema (se valida el DNI) y brinde sus datos y otras no lo necesitan.

b. Reglas de ventas de medicamentos:
Existen productos controlados y no controlados.
Los productos controlados requieren que se presente una receta no vencida. Existen 3 tipos de recetas: 
Receta normal: Tipo de receta que no vence y que se puede presentar en la botica varias veces para realizar una compra. No quiere compra presencial
Receta especial: Tipo de receta que tiene fecha de vencimiento y que es necesario comprar presencialmente.
Receta especial retenida: Tipo de receta que tiene fecha de vencimiento y sólo puede ser usada una vez, ya que una vez vendido el medicamento, queda retenida en la botica.

c. Regla para el registro de pago
Existen convenios con los seguros en la compra de medicamentos, en caso el cliente pertenezca a algún convenio, el sistema debe validar el DNI del cliente y verificar si tiene convenio con algún seguro. En caso tenga convenio activo, el cliente paga solamente el copago de la venta (por ejemplo, el 10% del total del producto). El registro del cliente se realiza en la central.

d. Reglas para ventas a clientes con línea de crédito
Existen clientes que tienen línea de crédito en la farmacia. Para realizar una compra a crédito, el cliente debe identificarse con su DNI.

e. Reglas para el despacho de medicamentos
Regla de vender primero los productos cuyo lote esté próximo a vencer (FEFO).

f. Regla de no vender medicamentos vencidos
Actualmente hay un control para los medicamentos vencidos, está prohibida la venta de medicamentos que vencen en los próximos 3 meses. Estos medicamentos son retirados de todos los almacenes mediante un control que realizan desde la central

g. No se puede anular una venta de un día anterior. 
Si la venta se realiza el día actual, se puede anular el comprobante. Si ya pasó 1 día, se aplica nota de crédito.


2. Inventario
- Descontar existencias automáticamente.
- Evitar vender sin stock.
- Listar Lotes y vencimientos
- Impedir la venta de productos vencidos.

3. Clientes
- Registrar clientes.
- Programa de fidelización a convenios (se realiza desde la central)

4. Productos
- Medicamentos.
- Productos OTC.
- Categorías.
- Laboratorios.
- Presentaciones.

6. Reportes
- Ventas por día.
- Reporte de incentivos del personal por ventas de determinados productos

Otras reglas de negocio que pueden aplicarse:
No cerrar la venta si el monto pagado es menor al total.
Una venta anulada debe devolver el stock.
No se puede eliminar una venta ya facturada.
La caja debe estar abierta para registrar una venta.