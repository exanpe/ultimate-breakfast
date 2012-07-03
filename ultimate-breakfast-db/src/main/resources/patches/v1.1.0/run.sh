#!/bin/ksh
#-------------------------------------------------------------------------------
# Objet: Execution des patchs de base de données version 2.10.2
#-------------------------------------------------------------------------------

echo "Execution des patchs de base de données version 2.10.2"

if [ $# -eq 3 ]; then
	cd scripts
	for script in `ls *.ksh `
	do
		echo "Execution script : "${script}
		./${script} $1 $2 $3
	done
else
	echo " Le nombre de parametres en entree n'est pas correct"
	echo 1.- nom utilisateur de la base de connexion
	echo 2.- mot de passe de la base de connexion
	echo 3.- oracle sid de la base de connexion
	
	exit 1
fi


RET=$?
echo "Code Retour du traitement: $RET"

if [ $RET -ne 0 ]
then
	echo "==> FIN ANORMALE patchDB"
else
	echo "==> FIN NORMALE patchDB"
fi

exit $RET