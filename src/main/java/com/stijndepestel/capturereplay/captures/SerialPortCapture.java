package com.stijndepestel.capturereplay.captures;

import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.xml.bind.DatatypeConverter;

import org.json.JSONObject;

import com.stijndepestel.capturereplay.Capture;
import com.stijndepestel.capturereplay.Persister;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public final class SerialPortCapture {

    private final String serialPortName;
    private final int[] serialPortParamsInts;
    private final boolean[] serialPortParamsBools;
    private final Optional<Function<SerialPortEvent, Boolean>> filter;
    private final Consumer<JSONObject> persister;

    public SerialPortCapture(final String serialPortName,
            final Consumer<JSONObject> persister) {
        this(serialPortName, persister, Optional.empty(), new int[] {},
                new boolean[] {});
    }

    public SerialPortCapture(final String serialPortName,
            final Consumer<JSONObject> persister,
            final Function<SerialPortEvent, Boolean> filter) {
        this(serialPortName, persister, Optional.of(filter), new int[] {},
                new boolean[] {});
    }

    public SerialPortCapture(final String serialPortName,
            final Consumer<JSONObject> persister,
            final Function<SerialPortEvent, Boolean> filter,
            final int[] serialPortParamsInts) {
        this(serialPortName, persister, Optional.of(filter),
                serialPortParamsInts, new boolean[] {});
    }

    public SerialPortCapture(final String serialPortName,
            final Consumer<JSONObject> persister,
            final Function<SerialPortEvent, Boolean> filter,
            final int[] serialPortParamsInts,
            final boolean[] serialPortParamsBools) {
        this(serialPortName, persister, Optional.of(filter),
                serialPortParamsInts, serialPortParamsBools);
    }

    public SerialPortCapture(final String serialPortName,
            final Consumer<JSONObject> persister,
            final int[] serialPortParamsInts,
            final boolean[] serialPortParamsBools) {
        this(serialPortName, persister, Optional.empty(), serialPortParamsInts,
                serialPortParamsBools);
    }

    private SerialPortCapture(final String serialPortName,
            final Consumer<JSONObject> persister,
            final Optional<Function<SerialPortEvent, Boolean>> filter,
            final int[] serialPortParamsInts,
            final boolean[] serialPortParamsBools) {
        // TODO check serial port parameters
        this.serialPortName = serialPortName;
        this.persister = persister;
        this.serialPortParamsInts = serialPortParamsInts;
        this.serialPortParamsBools = serialPortParamsBools;
        this.filter = filter;

    }

    public void start() throws SerialPortException {
        // CREATE NECESSARY OBJECTS FOR CAPTURE
        final Persister persister = new Persister(System.out);
        final Capture<Byte[]> capture = new Capture<Byte[]>(
                arr -> new JSONObject().put("bytearray",
                        DatatypeConverter
                                .printBase64Binary(this.unboxByteArray(arr))),
                this.persister);

        // CCreate serial port
        final SerialPort serialPort = new SerialPort(this.serialPortName);
        serialPort.openPort();

        // Set parameters as necessary.
        if (this.serialPortParamsInts.length == 4) {
            if (this.serialPortParamsBools.length == 2) {
                serialPort.setParams(this.serialPortParamsInts[0],
                        this.serialPortParamsInts[1],
                        this.serialPortParamsInts[2],
                        this.serialPortParamsInts[3],
                        this.serialPortParamsBools[0],
                        this.serialPortParamsBools[1]);
            } else {
                serialPort.setParams(this.serialPortParamsInts[0],
                        this.serialPortParamsInts[1],
                        this.serialPortParamsInts[2],
                        this.serialPortParamsInts[3]);
            }
        }

        // Set event listener
        serialPort.addEventListener(new SerialPortEventListener() {

            @Override
            public void serialEvent(final SerialPortEvent event) {
                // TODO test this filter
                SerialPortCapture.this.filter.ifPresent(f -> {
                    if (!f.apply(event)) {
                        return;
                    }
                });
                // assert event.getEventType() == SerialPort.MASK_RXCHAR;
                final int nrOfBytes = event.getEventValue();
                try {
                    System.out.print("x");
                    capture.capture(SerialPortCapture.this
                            .boxByteArray(serialPort.readBytes(nrOfBytes)));
                } catch (final SerialPortException e) {
                    e.printStackTrace();
                }
            }
        });

        final Scanner scan = new Scanner(System.in);
        System.out.println("Starting capture. Press return to stop.");
        capture.startCapture();
        // wait for return
        scan.nextLine();
        // stop capture
        capture.stopCapture();
        capture.saveEvents();
        scan.close();
    }

    private Byte[] boxByteArray(final byte[] arr) {
        final Byte[] boxedBytes = new Byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            boxedBytes[i] = arr[i];
        }
        return boxedBytes;
    }

    private byte[] unboxByteArray(final Byte[] arr) {
        final byte[] unboxedBytes = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            unboxedBytes[i] = arr[i];
        }
        return unboxedBytes;
    }

}
