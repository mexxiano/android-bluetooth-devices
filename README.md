# android-bluetooth-devices

El objetivo de esta aplicación es poder cargar un firmware en un microcontrolador AVR.
Por ejemplo, el firmare Sketch_Blink.hex que se encuentra en assets.

Para poder subir dicho firmware al controlador, se utiliza la herramienta avrdude.
Al llamar a dicha herramienta, se le deben pasar algunos parámetros para indicarle:
- la ubicación del firmware (*.hex)
- el puerto que se utilizará para realizar la conexión
- el baudrate o velocidad de transimisión de dicho archivo
- el tipo de microcontrolador que se desea programar
- y algunas otras varias carcaterísticas

# El Microcontrolador

Esta herramienta se utiliza para programar microcontroladores de la familia AVR (normalmente los que se utilizan en Arduino).

Los diferentes modelos de placas Arduinos utilizan diferentes microcontroladores. Es imporntante saber qué microcontrolador se desea programar.

# El Firmware

El firmware es el resultado final de compilar utilizando otras herramientas el código fuente, normalmente escrito en C++.
Este firmware por lo general se utiliza en format .hex, aunque también se utiliza .bin .elf.

# El puerto COM y el baudrate

Por lo general la comunicación se realiza mediante una conexión serie, utilizando algún puerto USB.  Antiguamente se hacía utilizando RS232.

En este caso en particular, la actualización del firmware se desea realizar utilizando un puerto serie Bluetooth.

Es importante también conocer el baudrate o velocidad de transmisión a utilizar, y eso dependerá del microcontrolador utilizado.
