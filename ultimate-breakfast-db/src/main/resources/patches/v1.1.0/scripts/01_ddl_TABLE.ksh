#!/bin/ksh
#------------------------------------------------------------------------
# Objet: Rajout du mapping dans V_DOSSIER_ADMINISTRATIF, la colonne DOM_VAL_LIB_R_SERIE_AFF a ete rajouté
#------------------------------------------------------------------------

if [ $# -eq 3 ]; then
	sqlplus -s $1\/$2@$3 @01_ddl_TABLE.sql 
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
	echo "==> FIN ANORMALE 01_ddl_TABLE"
else
	echo "==> FIN NORMALE 01_ddl_TABLE"
fi

exit $RET