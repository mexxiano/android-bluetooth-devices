# android-bluetooth-devices

El objetivo de esta aplicación es poder cargar un firmware en un microcontrolador AVR.
Por ejemplo, el firwmare ***Sketch_Blink.hex*** que se encuentra en ***assets***.

Para poder subir dicho firmware al microcontrolador, se utiliza la herramienta ***avrdude***.
Al llamar a dicha herramienta, se le deben pasar algunos parámetros para indicarle:
- la ubicación del firmware (*.hex)
- el puerto serie que se utilizará para realizar la conexión
- el baudrate o velocidad de transimisión de dicho firmware
- el tipo de microcontrolador que se desea programar
- y algunas otras varias carcaterísticas

# El Microcontrolador

Esta herramienta se utiliza para programar microcontroladores de la familia AVR (normalmente los que se utilizan en Arduino).

Los diferentes modelos de placas Arduinos utilizan diferentes microcontroladores. Es imporntante saber qué microcontrolador se desea programar.

# El Firmware

El firmware es el resultado final de compilar el código fuente, normalmente escrito en C++, utilizando algunas herramientas específicas para tal fin.
Este firmware por lo general se utiliza en format .hex, aunque también se utiliza .bin .elf.

# El puerto COM y el baudrate

Por lo general la comunicación se realiza mediante una conexión serie, utilizando algún puerto USB.  Antiguamente se hacía utilizando RS232.

En este caso en particular, la actualización del firmware se desea realizar utilizando un puerto serie Bluetooth.

Es importante también conocer el baudrate o velocidad de transmisión a utilizar, y eso dependerá del microcontrolador utilizado.

# Ejemplo de llamada a AVRDUDE

`avrdude -p m328p -c arduino -P /dev/ttyUSB0 -U signature:r:file.hex`

`avrdude -v -patmega328p -carduino -PCOM21 -b115200 -D -Uflash:w:serial.ino.hex:i`

# La app

Consta de dos botones: el primero permite listar y ver los dispositivos Bluetooth con los que nos 
hemos vinculado, y el segundo permite enviar a dicho dispositivo el firmware de prueba utilizando 
AVRDUDE como herramienta.