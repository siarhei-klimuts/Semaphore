/*
 * main.c
 *
 *  Created on: Jul 2, 2014
 *      Author: Siarhei
 */
#include <avr/io.h>
#include <avr/interrupt.h>
#include <avr/wdt.h>

#include "usbdrv/usbconfig.h"
#include "usbdrv/usbdrv.h"

#include <util/delay.h>

PROGMEM const char usbHidReportDescriptor[22] = { // USB report descriptor
    0x06, 0x00, 0xff,                       // USAGE_PAGE (Generic Desktop)
    0x09, 0x01,                             // USAGE (Vendor Usage 1)
    0xa1, 0x01,                             // COLLECTION (Application)
    0x15, 0x00,                             //    LOGICAL_MINIMUM (0)
    0x26, 0xff, 0x00,                       //    LOGICAL_MAXIMUM (255)
    0x75, 0x08,                             //    REPORT_SIZE (8)
    0x95, sizeof(uchar),   					//    REPORT_COUNT
    0x09, 0x00,                             //    USAGE (Undefined)
    0xb2, 0x02, 0x01,                       //    FEATURE (Data,Var,Abs,Buf)
    0xc0                                    // END_COLLECTION
};

uint8_t status = 0;

USB_PUBLIC uchar usbFunctionSetup(uchar data[8]) {
	usbRequest_t *rq = (void *)data; // cast data to correct type
	status = rq->wValue.bytes[0];
	return 0;
}

int main(void) {
    DDRD = 0x20;
    uchar i;

	wdt_enable(WDTO_1S);
	usbInit();
	usbDeviceDisconnect();

	for(i = 0; i<250; i++) { // wait 500 ms
		wdt_reset();
		_delay_ms(2);
	}

	usbDeviceConnect();

	sei();

	int8_t delay = 0;

	while(1) {
		wdt_reset();
		usbPoll();

		if(status == 1) {
			PORTD |= (1 << PD5);
		} else if(status == 2) {
			delay++;
			if(delay == 1) {
				PORTD |= (1 << PD5);
			} else if(delay == 100) {
				PORTD &=~(1 << PD5);
			} else if(delay == 200) {
				delay = 0;
			}
		} else {
			PORTD &=~(1 << PD5);
		}

		_delay_ms(2);
	}

	return 0;
}
