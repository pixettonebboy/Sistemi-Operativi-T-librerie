#!/bin/bash

# SINTASSI :  esame XXX

clear
echo "ESAME di ACHILLE PISANI";


# CONTROLLO PARAMETRI = = = = = = = = = = = = = = = = = = = = = = = =

# controllo n° argomenti (devono essere 5)
if [ $# -ne 4 ] ; then
    echo ERRORE: Numero argomenti errato;
    exit;
fi

# DIRIN deve essere una directory esistente
#if ! test -d $1; then
#    echo ERRORE: Directory DIRIN non esistente;
#    exit;
#fi

# EXT è una stringa di 4 caratteri, nella quale il primo elemento deve essere un punto (ovvero: il carattere ‘.’)
#if [[ ! $EXT = .??? ]]; then
#    echo "ERRORE: L'estensione deve essere formattata con un punto e 3 lettere";
#    exit;
#fi

# FOUT è il path assoluto di un file di testo esistente.
#if [[ ! "$FOUT" = ~/* ]] && [[ "$FOUT" = /* ]] ; then
#    echo "ERRORE: FOUT non è stato fornito come path assoluto";
#fi


# PRIMA CHIAMATA RICORSIONE = = = = = = = = = = = = = = = = = = = = =
# Il file recursion.sh è considerato nella stessa directory dell'invoker
"`pwd`/recursion.sh" "$DIR" "$EXT" ""
