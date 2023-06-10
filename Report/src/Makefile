all:
	pdflatex -file-line-error -draftmode -halt-on-error -shell-escape Relatorio 
	makeindex Relatorio.nlo -s nomencl.ist -o Relatorio.nls
	pdflatex -file-line-error -halt-on-error -shell-escape Relatorio
	bibtex Relatorio
	bibtex Relatorio
	pdflatex -file-line-error -halt-on-error -shell-escape Relatorio
	pdflatex -file-line-error -halt-on-error -shell-escape Relatorio
	pdflatex -halt-on-error -shell-escape Relatorio
clean:
	del /Q *.blg *.aux *.log *.dvi *.bbl *.toc *.lot *.lof *.idx *.ilg *.lol *.out *.run.xml *.synctex.gz *-blx.bib *.nlo *.nls _minted-Relatorio\*.* > nul 2>&1
