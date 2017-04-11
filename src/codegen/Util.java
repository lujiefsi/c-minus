package codegen;

public class Util {
	public static long ELFHash(String str){
		long hash = 0;
		long x = 0;
		int i = 0;
		for (i = 0; i < str.length(); i++) {
			hash = (hash << 4) + (long)(str.charAt(i));
			if ((x = hash & 0xF0000000L) != 0) {
				hash ^= (x >> 24);
			}
			hash &= ~x;
		}
		return hash;
	}
	public static boolean func_is_main(long funcname) {
		return ELFHash("main") == funcname;
	}
}
