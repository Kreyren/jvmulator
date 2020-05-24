/*
 * Copyright (c) 2020, Alex Blewitt, Bandlem Ltd
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.bandlem.jvm.jvmulator;
import static com.bandlem.jvm.jvmulator.Opcodes.BIPUSH;
import static com.bandlem.jvm.jvmulator.Opcodes.DADD;
import static com.bandlem.jvm.jvmulator.Opcodes.DCONST_0;
import static com.bandlem.jvm.jvmulator.Opcodes.DCONST_1;
import static com.bandlem.jvm.jvmulator.Opcodes.DDIV;
import static com.bandlem.jvm.jvmulator.Opcodes.DMUL;
import static com.bandlem.jvm.jvmulator.Opcodes.DNEG;
import static com.bandlem.jvm.jvmulator.Opcodes.DREM;
import static com.bandlem.jvm.jvmulator.Opcodes.DSUB;
import static com.bandlem.jvm.jvmulator.Opcodes.DUP;
import static com.bandlem.jvm.jvmulator.Opcodes.DUP2;
import static com.bandlem.jvm.jvmulator.Opcodes.DUP2_X1;
import static com.bandlem.jvm.jvmulator.Opcodes.DUP2_X2;
import static com.bandlem.jvm.jvmulator.Opcodes.DUP_X1;
import static com.bandlem.jvm.jvmulator.Opcodes.DUP_X2;
import static com.bandlem.jvm.jvmulator.Opcodes.FADD;
import static com.bandlem.jvm.jvmulator.Opcodes.FCONST_0;
import static com.bandlem.jvm.jvmulator.Opcodes.FCONST_1;
import static com.bandlem.jvm.jvmulator.Opcodes.FCONST_2;
import static com.bandlem.jvm.jvmulator.Opcodes.FDIV;
import static com.bandlem.jvm.jvmulator.Opcodes.FMUL;
import static com.bandlem.jvm.jvmulator.Opcodes.FNEG;
import static com.bandlem.jvm.jvmulator.Opcodes.FREM;
import static com.bandlem.jvm.jvmulator.Opcodes.FSUB;
import static com.bandlem.jvm.jvmulator.Opcodes.IADD;
import static com.bandlem.jvm.jvmulator.Opcodes.ICONST_0;
import static com.bandlem.jvm.jvmulator.Opcodes.ICONST_1;
import static com.bandlem.jvm.jvmulator.Opcodes.ICONST_2;
import static com.bandlem.jvm.jvmulator.Opcodes.ICONST_3;
import static com.bandlem.jvm.jvmulator.Opcodes.ICONST_4;
import static com.bandlem.jvm.jvmulator.Opcodes.ICONST_5;
import static com.bandlem.jvm.jvmulator.Opcodes.ICONST_M1;
import static com.bandlem.jvm.jvmulator.Opcodes.IDIV;
import static com.bandlem.jvm.jvmulator.Opcodes.IMUL;
import static com.bandlem.jvm.jvmulator.Opcodes.INEG;
import static com.bandlem.jvm.jvmulator.Opcodes.IREM;
import static com.bandlem.jvm.jvmulator.Opcodes.ISHL;
import static com.bandlem.jvm.jvmulator.Opcodes.ISHR;
import static com.bandlem.jvm.jvmulator.Opcodes.ISUB;
import static com.bandlem.jvm.jvmulator.Opcodes.IUSHR;
import static com.bandlem.jvm.jvmulator.Opcodes.LADD;
import static com.bandlem.jvm.jvmulator.Opcodes.LCONST_0;
import static com.bandlem.jvm.jvmulator.Opcodes.LCONST_1;
import static com.bandlem.jvm.jvmulator.Opcodes.LDIV;
import static com.bandlem.jvm.jvmulator.Opcodes.LMUL;
import static com.bandlem.jvm.jvmulator.Opcodes.LNEG;
import static com.bandlem.jvm.jvmulator.Opcodes.LREM;
import static com.bandlem.jvm.jvmulator.Opcodes.LSHL;
import static com.bandlem.jvm.jvmulator.Opcodes.LSHR;
import static com.bandlem.jvm.jvmulator.Opcodes.LSUB;
import static com.bandlem.jvm.jvmulator.Opcodes.LUSHR;
import static com.bandlem.jvm.jvmulator.Opcodes.NOP;
import static com.bandlem.jvm.jvmulator.Opcodes.POP;
import static com.bandlem.jvm.jvmulator.Opcodes.POP2;
import static com.bandlem.jvm.jvmulator.Opcodes.SIPUSH;
import static com.bandlem.jvm.jvmulator.Opcodes.SWAP;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
class JVMTest {
	private void expect(final double result, final byte[] code) {
		final JVM jvm = run(code);
		assertEquals(result, jvm.stack.pop().doubleValue());
		assertThrows(IndexOutOfBoundsException.class, jvm.stack::peek);
	}
	private void expect(final float result, final byte[] code) {
		final JVM jvm = run(code);
		assertEquals(result, jvm.stack.pop().floatValue());
		assertThrows(IndexOutOfBoundsException.class, jvm.stack::peek);
	}
	private void expect(final int result, final byte[] code) {
		final JVM jvm = run(code);
		assertEquals(result, jvm.stack.pop().intValue());
		assertThrows(IndexOutOfBoundsException.class, jvm.stack::peek);
	}
	private void expect(final long result, final byte[] code) {
		final JVM jvm = run(code);
		assertEquals(result, jvm.stack.pop().longValue());
		assertThrows(IndexOutOfBoundsException.class, jvm.stack::peek);
	}
	private JVM run(final byte[] code) {
		final JVM jvm = new JVM();
		jvm.setBytecode(code);
		jvm.run();
		return jvm;
	}
	@Test
	void testBadStackSwap() {
		final JVM jvm = new JVM();
		jvm.setBytecode(new byte[] {
				LCONST_0, DCONST_0, SWAP
		});
		jvm.step();
		jvm.step();
		assertThrows(IllegalStateException.class, jvm::step);
	}
	@Test
	void testConstantPush() {
		expect(10, new byte[] {
				BIPUSH, 0x0a
		});
		expect(314, new byte[] {
				SIPUSH, 0x01, 0x3a
		});
	}
	@Test
	void testDouble() {
		expect(1.0D, new byte[] {
				DCONST_0, DCONST_1, DADD
		});
		expect(-1.0D, new byte[] {
				DCONST_1, DCONST_0, DSUB
		});
		expect(4.0D, new byte[] {
				DCONST_1, DCONST_1, DADD, DCONST_1, DCONST_1, DADD, DMUL
		});
		expect(1.0D, new byte[] {
				DCONST_1, DCONST_1, DADD, DCONST_1, DCONST_1, DADD, DDIV
		});
		expect(0.0D, new byte[] {
				DCONST_1, DCONST_1, DADD, DCONST_1, DCONST_1, DADD, DREM
		});
		expect(1.0D, new byte[] {
				DCONST_1, DCONST_1, DADD, DCONST_1, DSUB, DNEG
		});
	}
	@Test
	void testFloat() {
		expect(3.0F, new byte[] {
				FCONST_0, FCONST_1, FCONST_2, FADD, FADD
		});
		expect(4.0F, new byte[] {
				FCONST_1, FCONST_1, FADD, FCONST_1, FCONST_1, FADD, FMUL
		});
		expect(1.0F, new byte[] {
				FCONST_1, FCONST_1, FADD, FCONST_1, FCONST_1, FADD, FDIV
		});
		expect(0.0F, new byte[] {
				FCONST_1, FCONST_1, FADD, FCONST_1, FCONST_1, FADD, FREM
		});
		expect(1.0F, new byte[] {
				FCONST_1, FCONST_1, FADD, FCONST_1, FSUB, FNEG
		});
	}
	@Test
	void testInteger() {
		expect(24, new byte[] {
				ICONST_4, ICONST_3, ICONST_1, ICONST_0, ICONST_M1, IADD, ISUB, IMUL, IMUL, INEG
		});
		expect(2, new byte[] {
				ICONST_5, NOP, ICONST_2, IREM
		});
		expect(1, new byte[] {
				ICONST_2, ICONST_5, IREM
		});
		expect(0, new byte[] {
				ICONST_5, ICONST_2, IDIV
		});
		expect(2, new byte[] {
				ICONST_2, ICONST_5, IDIV
		});
		expect(2, new byte[] {
				ICONST_1, ICONST_1, ISHL
		});
		expect(-1, new byte[] {
				ICONST_M1, ICONST_1, ISHR
		});
		expect(-1 >>> 1, new byte[] {
				ICONST_M1, ICONST_1, IUSHR
		});
	}
	@Test
	void testInvalid() {
		final JVM jvm = new JVM();
		jvm.setBytecode(new byte[] {
				(byte) 0xff
		});
		assertThrows(IllegalStateException.class, jvm::run);
	}
	@Test
	void testLong() {
		expect(1L, new byte[] {
				LCONST_0, LCONST_1, LADD
		});
		expect(4L, new byte[] {
				LCONST_1, LCONST_1, LADD, LCONST_1, LCONST_1, LADD, LMUL
		});
		expect(1L, new byte[] {
				LCONST_1, LCONST_1, LADD, LCONST_1, LCONST_1, LADD, LDIV
		});
		expect(0L, new byte[] {
				LCONST_1, LCONST_1, LADD, LCONST_1, LCONST_1, LADD, LREM
		});
		expect(1L, new byte[] {
				LCONST_1, LCONST_1, LADD, LCONST_1, LSUB, LNEG
		});
		expect(2L, new byte[] {
				LCONST_1, ICONST_1, LSHL
		});
		expect(-1L, new byte[] {
				LCONST_1, LNEG, ICONST_1, LSHR
		});
		expect(-1L >>> 1, new byte[] {
				LCONST_1, LNEG, ICONST_1, LUSHR
		});
	}
	@Test
	void testStackManipulation() {
		expect(1, new byte[] {
				ICONST_1, ICONST_0, POP
		});
		expect(1.0D, new byte[] {
				DCONST_1, DCONST_0, POP2
		});
		expect(-1, new byte[] {
				ICONST_0, ICONST_1, SWAP, ISUB
		});
		expect(2, new byte[] {
				ICONST_1, DUP, IADD
		});
		expect(2, new byte[] {
				ICONST_1, ICONST_0, DUP_X1, IADD, IADD
		});
		expect(2, new byte[] {
				ICONST_1, ICONST_0, ICONST_0, DUP_X2, IADD, IADD, IADD
		});
		expect(2.0D, new byte[] {
				DCONST_1, DUP2, DADD
		});
		expect(4, new byte[] {
				ICONST_1, ICONST_1, DUP2, IADD, IADD, IADD
		});
		expect(1.0D, new byte[] {
				DCONST_1, ICONST_5, DUP2_X1, POP2, POP
		});
		expect(2.0D, new byte[] {
				DCONST_1, DCONST_0, DUP2_X2, DADD, DADD
		});
	}
	@Test
	void testSupportedBytecodes() {
		// Contains the high water mark of implemented features
		final int max = 125;
		for (int b = 0; b < max; b++) {
			final String name = Opcodes.name((byte) b);
			// Not defined bytecodes
			if (name == null)
				continue;
			// Not supported yet
			if (name.contains("load") || name.contains("store") || name.contains("ldc"))
				continue;
			final JVM jvm = new JVM();
			int steps = 2;
			byte[] code;
			if (name.startsWith("l")) {
				code = new byte[] {
						LCONST_1, LCONST_0, (byte) b, 0x01, 0x02
				};
			} else if (name.startsWith("d")) {
				code = new byte[] {
						DCONST_1, DCONST_0, (byte) b, 0x01, 0x02
				};
			} else if (name.startsWith("f")) {
				code = new byte[] {
						FCONST_1, FCONST_0, (byte) b, 0x01, 0x02
				};
			} else {
				steps = 4;
				code = new byte[] {
						ICONST_4, ICONST_3, ICONST_2, ICONST_1, (byte) b, 0x01, 0x02
				};
			}
			if (name.endsWith("shr") || name.endsWith("shl")) {
				code[1] = ICONST_0;
			}
			jvm.setBytecode(code);
			while (steps-- > 0) {
				jvm.step();
			}
			assertDoesNotThrow(jvm::step, "Opcode " + name + " not supported (" + b + ")");
		}
	}
}
