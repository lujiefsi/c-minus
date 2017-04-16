package cminus;

import java.io.FileWriter;
import java.io.IOException;

import runtime.RunTime;
import semantic.Analysis;
import codegen.AstToNasm;
import grammar.Parser;
import grammar.TreeNode;
import lexic.Scan;
import lexic.TokenType;

public class Main {

	public static void main(String[] args) {
		if (args.length==0){
			System.err.println("ERROR:no input file");
			System.exit(1);
		}else if (args.length==1){
			System.err.println("ERROR:no output file");
			System.exit(1);
		}else if (args.length>2){
			System.err.println("ERROR:too many args");
			System.exit(1);
		}
		/*lexical analysis*/
		Scan scan = new Scan(args[0]);
		scan.setTraceScan(false);
		/*grammatical analysis*/
		Parser parser = new Parser(scan);
		TreeNode root = parser.parse();
		/*output AST to dot format,check the AST whether or not correct manually */
		DotWriter dotWriter = new DotWriter(args[0]+".dot");
		dotWriter.write(root);
		dotWriter.close();
		//*semantic analysis*/
		Analysis analysis = new Analysis(root);
		analysis.analysis();
		RunTime runTime = new RunTime(root,analysis.getGlobalSymbolTable());
		runTime.run();
		AstToNasm genNasm = new AstToNasm(args[1],analysis.getGlobalSymbolTable());
		genNasm.genCode(root,false);
		genNasm.close();
	}
	public static void dot(String file,TreeNode root){
		file = file+".dot";
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if (fileWriter!=null){
				try {
					fileWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
