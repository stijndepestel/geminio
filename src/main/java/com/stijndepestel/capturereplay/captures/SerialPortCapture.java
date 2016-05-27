package com.stijndepestel.capturereplay.captures;

import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.xml.bind.DatatypeConverter;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stijndepestel.capturereplay.Capture;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortException;

/**
 * Class to capture data from a serial port.
 *
 * @author sjdpeste
 *
 */
public final class SerialPortCapture {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(SerialPortCapture.class);

    /**
     * The amount of integer parameters that can be passed to the serial port.
     */
    private static final int SERIAL_PORT_INTEGER_PARAMS_LENGTH = 4;
    /**
     * The amount of boolean parameters that can be passed to the serial port.
     */
    private static final int SERIAL_PORT_BOOLEAN_PARAMS_LENGTH = 2;

    /**
     * Name of the serial port (e.g., /dev/ttyUSB0 ).
     */
    private final String serialPortName;
    /**
     * Parameters of the SerialPort (integers).
     */
    private final int[] serialPortParamsInts;
    /**
     * Parameters of the SerialPort (booleans).
     */
    private final boolean[] serialPortParamsBools;
    /**
     * Filter for the captured events. Can be used to prevent certain
     * SerialPortEvents to be captured.
     */
    private final Optional<Function<SerialPortEvent, Boolean>> filter;
    /**
     * Persister for the captured data.
     */
    private final Consumer<JSONObject> persister;

    /**
     * Create a new SerialPortCapture.
     *
     * @param serialPortName
     *            The name of the serial port on which to capture data.
     * @param persister
     *            The persister for the captured data.
     */
    public SerialPortCapture(final String serialPortName,
            final Consumer<JSONObject> persister) {
        this(serialPortName, persister, Optional.empty(), new int[] {},
                new boolean[] {});
    }

    /**
     * Create a new SerialPortCapture.
     *
     * @param serialPortName
     *            The name of the serial port on which to capture data.
     * @param persister
     *            The persister for the captured data.
     * @param filter
     *            A filter for the received serial port events to filter out
     *            events which should not be captured.
     */
    public SerialPortCapture(final String serialPortName,
            final Consumer<JSONObject> persister,
            final Function<SerialPortEvent, Boolean> filter) {
        this(serialPortName, persister, Optional.of(filter), new int[] {},
                new boolean[] {});
    }

    /**
     * Create a new SerialPortCapture.
     *
     * @param serialPortName
     *            The name of the serial port on which to capture data.
     * @param persister
     *            The persister for the captured data.
     * @param filter
     *            A filter for the received serial port events to filter out
     *            events which should not be captured.
     * @param serialPortParamsInts
     *            Parameters for the serial port, see
     *            {@link jssc.SerialPort#setParams(int, int, int, int)}.
     */
    public SerialPortCapture(final String serialPortName,
            final Consumer<JSONObject> persister,
            final Function<SerialPortEvent, Boolean> filter,
            final int[] serialPortParamsInts) {
        this(serialPortName, persister, Optional.of(filter),
                serialPortParamsInts, new boolean[] {});
    }

    /**
     * Create a new SerialPortCapture.
     *
     * @param serialPortName
     *            The name of the serial port on which to capture data.
     * @param persister
     *            The persister for the captured data.
     * @param filter
     *            A filter for the received serial port events to filter out
     *            events which should not be captured.
     * @param serialPortParamsInts
     *            Integer parameters for the serial port, see
     *            {@link jssc.SerialPort#setParams(int, int, int, int, boolean, boolean)}
     *            .
     * @param serialPortParamsBools
     *            Boolean parameters for the serial port, see
     *            {@link jssc.SerialPort#setParams(int, int, int, int, boolean, boolean)}
     *            .
     */
    public SerialPortCapture(final String serialPortName,
            final Consumer<JSONObject> persister,
            final Function<SerialPortEvent, Boolean> filter,
            final int[] serialPortParamsInts,
            final boolean[] serialPortParamsBools) {
        this(serialPortName, persister, Optional.of(filter),
                serialPortParamsInts, serialPortParamsBools);
    }

    /**
     * Create a new SerialPortCapture.
     *
     * @param serialPortName
     *            The name of the serial port on which to capture data.
     * @param persister
     *            The persister for the captured data.
     * @param serialPortParamsInts
     *            Integer parameters for the serial port, see
     *            {@link jssc.SerialPort#setParams(int, int, int, int, boolean, boolean)}
     *            .
     * @param serialPortParamsBools
     *            Boolean parameters for the serial port, see
     *            {@link jssc.SerialPort#setParams(int, int, int, int, boolean, boolean)}
     */
    public SerialPortCapture(final String serialPortName,
            final Consumer<JSONObject> persister,
            final int[] serialPortParamsInts,
            final boolean[] serialPortParamsBools) {
        this(serialPortName, persister, Optional.empty(), serialPortParamsInts,
                serialPortParamsBools);
    }

    /**
     * Create a new SerialPortCapture. Internal use.
     *
     * @param serialPortName
     *            The name of the serial port on which to capture data.
     * @param persister
     *            The persister for the captured data.
     * @param filter
     *            A filter for the received serial port events to filter out
     *            events which should not be captured. This is an Optional field
     *            since a filter is not obliged to exist.
     * @param serialPortParamsInts
     *            Integer parameters for the serial port, see
     *            {@link jssc.SerialPort#setParams(int, int, int, int, boolean, boolean)}
     *            .
     * @param serialPortParamsBools
     *            Boolean parameters for the serial port, see
     *            {@link jssc.SerialPort#setParams(int, int, int, int, boolean, boolean)}
     */
    private SerialPortCapture(final String serialPortName,
            final Consumer<JSONObject> persister,
            final Optional<Function<SerialPortEvent, Boolean>> filter,
            final int[] serialPortParamsInts,
            final boolean[] serialPortParamsBools) {
        // TODO check serial port parameters
        this.serialPortName = serialPortName;
        this.persister = persister;
        this.serialPortParamsInts = serialPortParamsInts.clone();
        this.serialPortParamsBools = serialPortParamsBools.clone();
        this.filter = filter;

    }

    /**
     * Start the capture on the serial port.
     *
     * @throws SerialPortException
     *             When something went wrong during the start of the capture.
     */
    public void start() throws SerialPortException {
        // Create capture object.
        final Capture<Byte[]> capture = new Capture<>(
                arr -> new JSONObject().put("bytearray",
                        DatatypeConverter.printBase64Binary(
                                SerialPortCapture.unboxByteArray(arr))),
                this.persister);

        // CCreate serial port
        final SerialPort serialPort = new SerialPort(this.serialPortName);
        serialPort.openPort();

        // Set parameters as necessary.
        if (this.serialPortParamsInts.length == SerialPortCapture.SERIAL_PORT_INTEGER_PARAMS_LENGTH) {
            if (this.serialPortParamsBools.length == SerialPortCapture.SERIAL_PORT_BOOLEAN_PARAMS_LENGTH) {
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
        serialPort.addEventListener(event -> {
            SerialPortCapture.this.filter.ifPresent(f -> {
                if (!f.apply(event)) {
                    return;
                }
            });
            try {
                System.out.print("x");
                final int nrOfBytes = event.getEventValue();
                capture.capture(SerialPortCapture
                        .boxByteArray(serialPort.readBytes(nrOfBytes)));
            } catch (final SerialPortException e) {
                SerialPortCapture.LOGGER.error(
                        "Something went wrong while reading the serial port input.",
                        e);
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

    /**
     * Box the byte array into a wrapper class array.
     *
     * @param arr
     *            The array of the primitive types.
     * @return The array of the wrapper types.
     */
    private static Byte[] boxByteArray(final byte[] arr) {
        final Byte[] boxedBytes = new Byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            boxedBytes[i] = Byte.valueOf(arr[i]);
        }
        return boxedBytes;
    }

    /**
     * Unbox the byte array into a primitive type array.
     *
     * @param arr
     *            The array of the wrapper types.
     * @return The array of the primitive types.
     */
    private static byte[] unboxByteArray(final Byte[] arr) {
        final byte[] unboxedBytes = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            unboxedBytes[i] = arr[i].byteValue();
        }
        return unboxedBytes;
    }

}
