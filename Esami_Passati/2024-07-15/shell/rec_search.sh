# Recursively search through a directory's subtree
# "$recursive_command" "$dir" "$M" "$outfile"

cd "$1"
SUM=0
for file in * ; do
    if [[ -f "$file" ]] ; then
        U=`ls -l "$i" | awk '{print $3}'` 
        if [ "$USER" = "$U" ]; then
            X=`head -n3 "$file" | wc -w` 
            SUM=`expr $SUM + $X`
        fi
    elif [[ -d "$file" ]] ; then
        "$0" "$file" "$2" "$3"
    fi
done

if [ $SUM -gt $2 ]; then
    echo `pwd` $SUM >> "$3"
fi