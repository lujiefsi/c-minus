package codegen;

import grammar.NodeType;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class NasmCode {
	private boolean bss_mark;
	private PrintWriter disk;
	private boolean data_mark;
	private boolean text_mark;

	public NasmCode(String file){
		try {
			disk = new PrintWriter(new FileOutputStream(file));
			bss_mark = false;

		} catch (FileNotFoundException e) {
			System.out.println("Error in opening output file");
		}
	}
	private void emitCode(String code) {
		disk.println( code );
	}
	public void close() {
		if (disk!=null){
			disk.close();
		}
	}
	public void code_start_bss(){
		if (!bss_mark){
			emitCode("section .bss");
			bss_mark = true;
		}
	}

	public void code_declare_global_var(String varname, int size) {
		emitCode("D_"+Util.ELFHash(varname)+":\t"+"resw "+2*size);
	}
	public boolean code_data_section() {
		if (!data_mark){
			data_mark = true;
			emitCode("section .data");
			emitCode("numbers db \"0123456789\", 0xA");
			emitCode("inputchar db 0");
			return false;
		}
		return true;
	}
	public void code_start_text() {
		if (!text_mark){
			emitCode("section .text");
			text_mark = true;
		}
		
	}
	public void code_start_func(long funcname) {
		if (Util.func_is_main(funcname)) {
			emitCode("global _start\n_start:");
		} else {
			emitCode("global F_"+funcname+"\nF_"+funcname+":");
		}
		emitCode("push\tebp");
		emitCode("mov\tebp, esp");
		
	}

	public void code_end_func(long funcname) {
		emitCode("leave");
		if (!Util.func_is_main(funcname)) {
			emitCode("ret");
		} else {
			code_end_main();
		}
		
	}
	private void code_end_main() {
		emitCode("mov ebx, eax\nmov eax,1\nint 80h");
	}
	public void code_func_input() {
		emitCode("sub esp, 4\nmov dword [ebp-4], 0\nmov byte [inputchar], 0\njmp G6");
		emitCode("G5:\nmov dword eax, [ebp-4]\nmov ebx, 10\nmul ebx\nxor ecx, ecx");
		emitCode("mov byte cl, [inputchar]\nsub ecx, 48\nadd eax, ecx\nmov dword [ebp-4], eax");
		emitCode("G6:\nmov eax, 03h\nmov ebx, 00h\nmov ecx, inputchar\nmov edx, 01h");
		emitCode("int 80h\ncmp byte [inputchar], 0ah\njne G5\nmov dword eax, [ebp-4]");
		
	}
	public void code_func_output() {
		/*code_start_func(); */
		emitCode("sub esp, 4\nmov dword [ebp-4], 0\njmp G2\nG1:\nadd dword [ebp-4], 1\npush edx");
		emitCode("G2:\nmov edx, 0\nmov eax, [ebp+8]\nmov ebx, 10\ndiv ebx\nmov [ebp+8], eax");
		emitCode("cmp eax, 0\njnz G1\npush edx\nadd dword [ebp-4], 1\njmp G3");
		emitCode("G4:\nsub dword [ebp-4], 1\npop edx\nmov eax, 4\nmov ebx, 1\nmov ecx, numbers");
		emitCode( "add ecx, edx\nmov edx, 1\nint 80h");
		emitCode("G3:\ncmp dword [ebp-4], 0\njnz G4\nmov eax, 4\nmov ebx, 1\nlea ecx, [numbers+10]");
		emitCode("mov edx, 1\nint 80h");
		
	}
	public void code_push_ind(int offset) {
		emitCode("push\tdword "+getVarStr(offset));
		
	}
	private String getVarStr(int offset){
		return offset>=0?"[ebp+"+offset+"]":"[ebp"+offset+"]";
	}
	public void code_push_cons(int num){
		emitCode("push\tdword "+num);
	}
	public void code_pop(int reg) {
		emitCode("pop\t"+getRegStr(reg));
	}
	private String getRegStr(int reg){
		switch (reg) {
		case 1:
			return "eax";
		case 2:
			return "ebx";
		case 3:
			return "ecx";
		case 4:
			return "edx";
		}

		return null;
	}
	public void code_op_binary(int reg1, int reg2, NodeType op) {
		String regStr1 = getRegStr(reg1);
		String regStr2 = getRegStr(reg2);
		if (op.equals(NodeType.PLUS)){
			emitCode("lea\teax, ["+regStr1+"+"+regStr2+"]");
		}else if (op.equals(NodeType.MINUS)){
			emitCode("sub\t"+regStr1+", "+regStr2);
		}else if (op.equals(NodeType.MULT)){
			emitCode("imul\t"+regStr1+","+regStr2);
		}else if (op.equals(NodeType.OVER)){
			emitCode("mov\tedx,"+regStr1);
			emitCode("sar\tedx,31");
			emitCode("idiv\t"+regStr2);
		}
		else{
			emitCode("cmp\t"+regStr1+", "+regStr2);
			if (op.equals(NodeType.EQ)){
				emitCode("sete\tal");
			}else if (op.equals(NodeType.LT)){
				emitCode("setl\tal");
			}else if (op.equals(NodeType.LE)){
				emitCode("setle\tal");
			}
			emitCode("movzx\teax, al");
		}
	}
	public void code_push_reg(int reg, boolean mem) {
		String regStr = getRegStr(reg);
		if (mem){
			emitCode("push\tdword ["+regStr+"]");
		}else{
			emitCode("push\tdword "+regStr);
		}
	}
	public void code_test_condition(int reg, int test, int label) {
		emitCode("cmp\t"+getRegStr(reg)+", "+test);
		emitCode("je\tL"+label);
	}
	public void code_jmp(int label) {
		emitCode("jmp\tL"+label);
	}
	public void code_label(int label) {
		emitCode("L"+label+":");
	}
	public void code_call_func(long currentFun) {
		emitCode("call\tF_"+currentFun);
	}
	public void code_clean_stack(int height) {
		emitCode("add\tesp, "+height);
	}
	public void code_sub_esp(int size) {
		emitCode("sub\tesp, "+size);
	}
	public void code_op_assign(int target, int source) {
		emitCode("mov\tdword ["+getRegStr(target)+"], "+getRegStr(source));
	}
	public void code_lea_local(int reg, int offset) {
		emitCode("lea\t"+getRegStr(reg)+", "+getVarStr(offset));
	}
	public void code_push_global_array(long elfHash) {
		emitCode("push\tdword D_"+elfHash);
	}
	public void code_lea_global(int target, long addr, int offset) {
		String array = getRegStr(offset);
		if (array ==null){
			emitCode("lea\t"+getRegStr(target)+", [D_"+addr+"]");
		}else{
			emitCode("lea\t"+getRegStr(target)+", [D_"+addr+"+"+array+"]");
		}
	}
	public void code_get_array_offset(int baseoff, int reg, int varLength, int global) {
		String regStr = getRegStr(reg);
		if (global==0){
			emitCode("mov\tebx,ebp");
		}else if (global == -1){
			emitCode("mov\tebx, [ebp+"+baseoff+"]");
		}else{
			emitCode("mov\tebx, 0");
		}
		emitCode("imul\t"+regStr+", "+varLength);
		emitCode("add\tebx, eax");
		if (global == 0){
			emitCode("sub\t,ebx, "+Math.abs(baseoff));
		}
	}
	public void code_move_reg(int target, int source) {
		if (target == source) return;
		emitCode("move\t"+getRegStr(target)+","+getRegStr(source));
	}
	public void code_push_mem(long addr, int reg) {
		emitCode("push\tdword [D_"+addr+"+"+getRegStr(reg)+"]");
	}
	public void code_push_global_var(long var) {
		emitCode("push\tdword [D_"+var+"]");
	}
}
