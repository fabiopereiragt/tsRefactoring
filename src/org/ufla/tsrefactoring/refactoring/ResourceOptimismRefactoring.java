package org.ufla.tsrefactoring.refactoring;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.ufla.tsrefactoring.dto.ResultTestSmellDTO;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;

public class ResourceOptimismRefactoring {

	public static boolean executeRefactory(ResultTestSmellDTO resourceOptimismTestSmell) throws FileNotFoundException {

		File file = new File(resourceOptimismTestSmell.getFilePath());
		CompilationUnit cu = StaticJavaParser.parse(file);

		/*
		 * Adicionar instrução(statement) Só é possível adicionar uma linha de comando
		 * no BlockStmt Daí, primeiramente vc precisa buscar o BlockStmt de onde vc quer
		 * inserir o comando A primeira linha, busca o bloco de comando da qual onde
		 * será inserido a instrução A segunda linha, o primeiro parâmetro informa o
		 * index da posição onde adicionar no bloco e o segundo parâmetro, a expressão
		 * em string, como por exemplo: "System.out.println(\"Hello World\");" BlockStmt
		 * block = n.findAncestor(BlockStmt.class).get();
		 * block.addStatement(POSIÇÃO_INDEX_ONDE_COLOCAR__EXPRESSAO, STRING_STATEMENT);
		 * 
		 * OU PODE FAZER ASSIM:
		 *
		 * Cria a instrução: Statement staticStatement =
		 * StaticJavaParser.parseStatement("System.out.println(\"Hello World\");");
		 * 
		 * Localiza o bloco onde quer inserir e insere na posição (index) do bloco
		 * n.findAncestor(BlockStmt.class).ifPresent(x -> { x.addStatement(0,
		 * staticStatement); });
		 */

		cu.accept(new ModifierVisitor<Void>() {

			@Override
			public Visitable visit(VariableDeclarator n, Void arg) {
				if ((n.getType().asString().equals("File"))
						&& (n.getBegin().get().line == resourceOptimismTestSmell.getLineNumber())) {

					// Instrução a ser adicionada
					// Statement staticStatement =
					// StaticJavaParser.parseStatement("System.out.println(\"Hello World\");");
					Statement staticStatementA = StaticJavaParser.parseStatement(
							"if (!" + n.getNameAsString() + ".exists()){" + "throw new FileNotFoundException(\"The file "
									+ n.getNameAsString() + " does not exist.\");}");

					// Busca o Bloco de instruções de qual "n" pertence. IMPORTANTE: só é possível
					// adicionar statement em Bloco de Instruções (BlockStmt)
					BlockStmt block = n.findAncestor(BlockStmt.class).get();

					// Converte o block de instruções em uma lista de nós com os filhos do bloco.
					// Isso foi feito para identificar o índice da instrução que foi localizada,
					// pois será acrescentada uma instrução logo abaixo dela
					List<Node> blocks = block.getChildNodes();

					// Converte os nós em string para comparar e conseguir localizar qual nó é
					// referente ao "n". Após, pega o index do nó encontrado.
					int index = blocks.toString().indexOf(n.getParentNode().get().toString());

					/*
					 * blocks.forEach(x -> { //System.out.println(x);
					 * System.out.println(x.toString().contains(n.getParentNode().get().toString()))
					 * ; });
					 */
					// System.out.println(n.getParentNode().get());

					// Busca o Bloco de instruções de qual "n" pertence novamente (poderia colocar
					// diretamente "block.addStatement"
					// Verifica se existe e insere a instrução na posição (index)
					n.findAncestor(BlockStmt.class).ifPresent(x -> {
						// x.addStatement(0, staticStatement1);
						x.addStatement(index, staticStatementA);
					});

				}
				// System.out.println(cu.toString());
				return super.visit(n, arg);
			}

		}, null);
		try {
			// The second parameter says to append the file.
			// False, the file will be cleared before writing
			FileWriter fw = new FileWriter(file, false);
			fw.write(cu.toString());
			fw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

}

