# Recursively search through a directory's subtree
# "$recursive_command" "$Xuser" "$Yuser" "$str" "$dir"

cd "$4"
echo "Analysis of `pwd`"
nX=0
nY=0
for file in * ; do
    if [[ -f "$file" ]] ; then
        if [[ "$file" = *"$3" ]]; then
            if [[ $1 = `ls -l "$file" |awk '{print $3}'` ]]; then
                nX=`expr $nX + 1`
                echo "`pwd`/$file nX=$nX" 
            elif [[ $2 = `ls -l "$file" |awk '{print $3}'` ]]; then
                nY=`expr $nY + 1`
                echo "`pwd`/$file nY=$nY" 
            fi
        fi
    elif [[ -d "$file" ]] ; then
        #echo "Starting recursion on dir `pwd`/$file"
        "$0" "$1" "$2" "$3" "$file"
    fi
done

r=`expr $nX - $nY`
if [[ $nX -gt $nY ]]; then
    echo `pwd` $r
    echo `pwd` $r >> $HOME/report
fi