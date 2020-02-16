TOPULL=$1
TOMERGE=$2
if [[ $# != 2 ]] ; then
  echo "Arg1: nome branch da cui prelevare dati\nArg2: nome branch dove inserire dati"
  exit 1
fi
git checkout ${TOPULL}
git pull
git checkout ${TOMERGE}
git merge ${TOPULL}
