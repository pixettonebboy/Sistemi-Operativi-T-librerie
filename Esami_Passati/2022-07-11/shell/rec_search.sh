# Recursively search through a directory's subtree
# "$recursive_command" "$stringa" "$dir" "$outfile"

cd "$2"
Y=0
X=0

for file in * ; do
    if [[ -f "$file" ]] ; then
        nL=`grep -c "$1" "$file"`
        echo "Analysis of $file nL=$nL"
        
        if [[ $nL -ge 1 ]]; then
            Y=`expr $Y + 1`
        fi
        X=`expr $X + 1`
    elif [[ -d "$file" ]] ; then
        "$0" "$1" "$file" "$3"
    fi
done

Xmezzi=`expr $X / 2`
if [[ $Y -ge $Xmezzi  ]]; then
    echo `pwd` $Y >> $3
fi