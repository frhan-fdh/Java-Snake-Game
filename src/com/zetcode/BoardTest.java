package com.zetcode;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BoardTest {
    private Board board;

    @Before
    public void setUp() throws Exception {
        System.setProperty("java.awt.headless", "true"); // Hindari GUI
        board = new Board();
    }

    // Helper untuk akses field private
    private Object getPrivateField(String fieldName) throws Exception {
        Field field = Board.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(board);
    }

    // Helper untuk panggil method private
    private void invokePrivateMethod(String methodName) throws Exception {
        Method method = Board.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        method.invoke(board);
    }

    // ===== TEST CASES =====

    @Test
    public void testUlarAwalPanjang3() throws Exception {
        int dots = (int) getPrivateField("dots");
        assertEquals("Ular harus punya 3 titik di awal", 3, dots);
    }

    @Test
    public void testGerakKanan() throws Exception {
        // Ambil posisi X awal
        int[] x = (int[]) getPrivateField("x");
        int posisiAwal = x[0];

        // Gerakkan ular
        invokePrivateMethod("move");

        // Cek posisi X bertambah 10 pixel
        assertEquals(posisiAwal + 10, x[0]);
    }

    @Test
    public void testMakanApel() throws Exception {
        // Set posisi kepala ular = posisi apel
        int[] x = (int[]) getPrivateField("x");
        int[] y = (int[]) getPrivateField("y");
        setPrivateField("apple_x", x[0]);
        setPrivateField("apple_y", y[0]);

        // Cek apel dimakan
        invokePrivateMethod("checkApple");

        int dots = (int) getPrivateField("dots");
        assertEquals("Panjang ular harus nambah 1", 4, dots);
    }

    @Test
    public void testTabrakanTembok() throws Exception {
        // Set kepala ular di luar batas
        int[] x = (int[]) getPrivateField("x");
        x[0] = 300; // Melebihi B_WIDTH (300)

        invokePrivateMethod("checkCollision");

        boolean inGame = (boolean) getPrivateField("inGame");
        assertFalse("Game harus berhenti saat nabrak tembok", inGame);
    }



    // ===== HELPER TAMBAHAN =====
    private void setPrivateField(String fieldName, Object value) throws Exception {
        Field field = Board.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(board, value);
    }
}