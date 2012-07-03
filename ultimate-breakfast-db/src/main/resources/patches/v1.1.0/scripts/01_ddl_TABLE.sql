-- *****************************************************************************
-- *   Nom du script : 01_ddl_V_DOSSIER_ADMINISTRATIF.sql
-- *****************************************************************************

spool ../logs/01_ddl_V_DOSSIER_ADMINISTRATIF.log

whenever OSERROR exit SQL.OSCODE;

prompt debut 01_ddl_V_DOSSIER_ADMINISTRATIF.sql

--===================================
--  PARTIE REENTRANTE
--===================================

prompt partie reentrante

-- Pas de partie reentrante car le create or replace, ne genere pas pas d'erreur lors de multiple execution

prompt fin partie reentrante

--===================================
--  Partie non réentrante
--===================================

WHENEVER SQLERROR EXIT SQL.SQLCODE;
CREATE OR REPLACE FORCE VIEW "V_DOSSIER_ADMINISTRATIF" ("ID", "ID_DOSSIER", "ID_EMPLOI", "ACTIF", "NOM", "PRENOM", "NOM_MARITAL", "DATE_DE_NAISSANCE", "SEXE", "MATRICULE_ARMEE", "IDENTIFIANT_DEFENSE", "SMU_AFFECTATION", "MESSAGE", "DATE_DEB_ACTIVITE", "DATE_FIN_ACTIVITE", "COMPAGNIE_AFFECTATION", "DATE_DEBUT_AFFECTATION_CIE", "DATE_FIN_AFFECTAION_CIE", "SMU_DETACHEMENT", "COMPAGNIE_DETACHEMENT", "DATE_DEBUT_DETACHEMENT_CIE", "DATE_FIN_DETACHEMENT_CIE", "TELEPHONE_AFFECTATION", "TELEPHONE_DETACHEMENT", "TELEPHONE_PORTABLE", "TELEPHONE_FIXE", "GRADE", "SPECIALITE", "UNITE_AFFECTATION", "UNITE_DETACHEMENT", "VERROUS", "ID_USER_VERROUS", "DATE_VERROUS", "DOS_INDIC_SAISI_GRP", "SOUS_UNITE_AFFECTATION", "SOUS_UNITE_DETACHEMENT", "DOS_DAT_CREATION", "SMU_ASSOCIE", "DOS_SMU_EXCLUS", "DOM_VAL_LIB_R_SERIE_AFF", "DOM_VAL_LIB_RSERIEDET")
AS
  SELECT
    /*+ RULE */
    dd.dos_id,
    dd.dos_id,
    e.emp_id,
    dd.dos_statut,
    dd.dos_nom,
    dd.dos_prenom,
    dd.dos_nom_mari,
    dd.dos_dat_naissance,
    dd.dom_val_rsex,
    dd.dos_matri_armee,
    dd.dos_ident_defense,
    e.dom_val_lib_ruf_aff, --e.dom_val_lib_rcss_aff,
    'O' MESSAGE,
    e.emp_dat_deb_activite,
    e.emp_dat_fin_activite,
    e.dom_val_lib_rcss_aff,
    e.emp_dat_deb_aff,
    e.emp_dat_fin_aff,
    e.dom_val_lib_ruf_det, --e.dom_val_lib_rcss_det,
    e.dom_val_lib_rcss_det,
    e.emp_dat_deb_det,
    e.emp_dat_fin_det,
    e.emp_tel_travail_aff,
    e.emp_tel_travail_det,
    dd.dos_tel_portable tel_portable,
    dd.dos_tel_domicile tel_fixe,
    e.dom_val_lib_rgr,
    e.emp_nom_specialite,
    e.dom_val_lib_ruf_aff,
    e.dom_val_lib_ruf_det,
    dd.dos_ind_verrou,
    dd.uti_id_verrou,
    dd.dos_dat_verrou,
    dd.dos_indic_saisi_grp,
    e.dom_val_lib_rusf_aff,
    e.dom_val_lib_rusf_det,
    dd.dos_dat_creation,
    (SELECT r.r_str_nom
    FROM dossier_smu_associe dsa,
      r_structure r
    WHERE r.r_str_id  = dsa.r_str_id
    AND dsa.dos_id(+) = dd.dos_id
    AND ROWNUM        = 1
    ) structure_associee,
    dd.dos_smu_exclus,
    e.dom_val_lib_rserieaff,
    e.dom_val_lib_rseriedet
  FROM dossier dd,
    r_structure rs,
    t_emploi e
  WHERE dd.dos_id        = e.dos_id(+)
  AND rs.r_str_id(+)     = e.r_str_id_aff
  AND e.for_ind_histo(+) = 'N';

-- Code ne devant pas échouer

prompt fin 01_ddl_V_DOSSIER_ADMINISTRATIF.sql

spool off
exit
