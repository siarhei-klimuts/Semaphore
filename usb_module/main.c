/*
 * main.c
 *
 *  Created on: Jul 2, 2014
 *      Author: Siarhei
 */
#include <avr/io.h>
#include <avr/interrupt.h>
#include <avr/pgmspace.h>
#include <avr/wdt.h>
#include <util/delay.h>

#include "usbdrv/usbconfig.h"
#include "usbdrv/usbdrv.h"

#define NOTES_COUNT 39
#define SERIALIZE_DEVIDER 10
#define PORT_D_THRESHOLD 40
#define PLAY_ON_STATUS 30
#define FREQUENCY_DEV 5000
#define DURATION_MULTIPLIER 15
#define TICK_DELAY 100 //us
#define FREQUECY_EDGE 3
#define BLINK_DELAY 2000

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

PROGMEM const uint16_t frequences[NOTES_COUNT] = {
  392, 392, 392, 311, 466, 392, 311, 466, 392,
  587, 587, 587, 622, 466, 369, 311, 466, 392,
  784, 392, 392, 784, 739, 698, 659, 622, 659,
  415, 554, 523, 493, 466, 440, 466,
  311, 369, 311, 466, 392 };

PROGMEM const uint16_t durations[NOTES_COUNT] = {
  350, 350, 350, 250, 100, 350, 250, 100, 700,
  350, 350, 350, 250, 100, 350, 250, 100, 700,
  350, 250, 100, 350, 250, 100, 100, 100, 450,
  150, 350, 250, 100, 100, 100, 450,
  150, 350, 250, 100, 750 };

volatile uint8_t *blinkPort = &PORTB;
uint8_t pin;
uint8_t isBlink;
uint8_t play;

USB_PUBLIC uchar usbFunctionSetup(uchar data[8]) {
	usbRequest_t *rq = (void *)data; // cast data to correct type
	uint8_t status = rq->wValue.bytes[0]; //40/41 - 20/21 - 30/31
    PORTD &=~ ((1 << PD5) | (1 << PD4));
    PORTB &=~ ((1 << PD2) | (1 << PD3));

	if(status < PORT_D_THRESHOLD) {
		blinkPort = &PORTB;
	} else {
		blinkPort = &PORTD;
	}

	isBlink = status % SERIALIZE_DEVIDER;
	pin = status / SERIALIZE_DEVIDER;
	play = status % PLAY_ON_STATUS == 0;

	return 0;
}

int main(void) {
    DDRD = 0x30;
    DDRB = 0xC;

	wdt_enable(WDTO_1S);
	usbInit();
	usbDeviceDisconnect();

	for(uchar i = 0; i<250; i++) { // wait 500 ms
		wdt_reset();
		_delay_ms(2);
	}

	usbDeviceConnect();

	sei();

	int16_t blinkDelay = 0;
	uint16_t soundDelay = 0;
	uint8_t note = 0;
	uint16_t duration = 0;
	uint16_t frequency = 0;

	while(1) {
		wdt_reset();
		usbPoll();

		blinkDelay++;
		if(blinkDelay > BLINK_DELAY) {
			blinkDelay = -BLINK_DELAY;
		}

		if(isBlink == 0) {
			*blinkPort |= (1 << pin);
		} else {
			if(blinkDelay == 0) {
				*blinkPort ^= (1 << pin);
			}
		}

		if(play == 1) {
			if(soundDelay < duration) {
				if(soundDelay % frequency <= FREQUECY_EDGE) {
					PORTD ^= (1 << PD5);
				}
			} else {
				soundDelay = 0;
				if(note == NOTES_COUNT) {
					note = 0;
					play = 0;
				}

				duration = pgm_read_word_near(durations + note) * DURATION_MULTIPLIER;
				frequency = FREQUENCY_DEV / pgm_read_word_near(frequences + note);

				note++;
			}

			soundDelay++;
		}

		_delay_us(TICK_DELAY);
	}

	return 0;
}
