/*
 * Copyright (c) 2020, Alex Blewitt, Bandlem Ltd
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.bandlem.jvm.jvmulator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
public class SlotTest {
	private static final Slot booleanFalseSlot = Slot.of(false);
	private static final Slot booleanTrueSlot = Slot.of(true);
	private static final Slot doubleSlot = Slot.of(4.0d);
	private static final Slot empty = Slot.empty();
	private static final Slot floatSlot = Slot.of(2.0f);
	private static final Slot intSlot = Slot.of(1);
	private static final Slot longSlot = Slot.of(3L);
	static void wrapSlotInSlot() {
		Slot.of(Slot.of("Hello World"));
	}
	@Test
	void testBooleanFalseSlot() {
		final Slot slot = booleanFalseSlot;
		assertEquals(0, slot.intValue());
		assertEquals(false, slot.booleanValue());
		assertEquals(0, slot.toObject());
		assertEquals("0", slot.toString());
		assertFalse(slot.isWide());
		assertThrows(ClassCastException.class, slot::longValue);
		assertThrows(ClassCastException.class, slot::floatValue);
		assertThrows(ClassCastException.class, slot::doubleValue);
	}
	@Test
	void testBooleanTrueSlot() {
		final Slot slot = booleanTrueSlot;
		assertEquals(1, slot.intValue());
		assertEquals(true, slot.booleanValue());
		assertEquals(1, slot.toObject());
		assertEquals("1", slot.toString());
		assertFalse(slot.isWide());
		assertThrows(ClassCastException.class, slot::longValue);
		assertThrows(ClassCastException.class, slot::floatValue);
		assertThrows(ClassCastException.class, slot::doubleValue);
	}
	@Test
	void testDoubleSlot() {
		final Slot slot = doubleSlot;
		assertEquals(4, slot.doubleValue());
		assertEquals(4.0D, slot.toObject());
		assertEquals("4.0", slot.toString());
		assertTrue(slot.isWide());
		assertThrows(ClassCastException.class, slot::intValue);
		assertThrows(ClassCastException.class, slot::longValue);
		assertThrows(ClassCastException.class, slot::floatValue);
	}
	@Test
	void testEmptySlot() {
		final Slot slot = empty;
		assertEquals("---", slot.toString());
		assertEquals(null, slot.toObject());
		assertFalse(slot.isWide());
		assertThrows(ClassCastException.class, slot::intValue);
		assertThrows(ClassCastException.class, slot::longValue);
		assertThrows(ClassCastException.class, slot::floatValue);
		assertThrows(ClassCastException.class, slot::doubleValue);
	}
	@Test
	void testFloatSlot() {
		final Slot slot = floatSlot;
		assertEquals(2F, slot.floatValue());
		assertEquals(2.0F, slot.toObject());
		assertEquals("2.0", slot.toString());
		assertFalse(slot.isWide());
		assertThrows(ClassCastException.class, slot::intValue);
		assertThrows(ClassCastException.class, slot::longValue);
		assertThrows(ClassCastException.class, slot::doubleValue);
	}
	@Test
	void testIntSlot() {
		final Slot slot = intSlot;
		assertEquals(1, slot.intValue());
		assertEquals(true, slot.booleanValue());
		assertEquals(1, slot.toObject());
		assertEquals("1", slot.toString());
		assertFalse(slot.isWide());
		assertThrows(ClassCastException.class, slot::longValue);
		assertThrows(ClassCastException.class, slot::floatValue);
		assertThrows(ClassCastException.class, slot::doubleValue);
	}
	@Test
	void testLongSlot() {
		final Slot slot = longSlot;
		assertEquals(3L, slot.longValue());
		assertEquals(3L, slot.toObject());
		assertEquals("3", slot.toString());
		assertTrue(slot.isWide());
		assertThrows(ClassCastException.class, slot::intValue);
		assertThrows(ClassCastException.class, slot::floatValue);
		assertThrows(ClassCastException.class, slot::doubleValue);
	}
	@Test
	void testReferenceSlot() {
		final Slot slot = Slot.of("Hello World");
		assertEquals("Hello World", slot.referenceValue());
		assertEquals("Hello World", slot.toString());
		assertEquals("Hello World", slot.toObject());
		assertNull(Slot.of(null).referenceValue());
		assertThrows(IllegalStateException.class, SlotTest::wrapSlotInSlot);
	}
}
