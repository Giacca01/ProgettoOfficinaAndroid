<?php
    /*Questo WS restituisce l'elenco delle macchine presenti sul DB */
    $con = new mysqli("localhost", "root", "", "officina");
    /*Controlla se il codice di errore è diverso da 0*/
    if ($con->connect_errno)
        die("Errore connessione database " . $con->connect_errno . " " . $con->connect_error);
    $sql = "SELECT idAuto, marca, modello FROM auto";
    /*Il metodo query lancia la query sql e restituisce il recordset corrispondente*/
    $rs = $con->query($sql);
    /*Controlla se il recordset esiste o no cioè se ci sono stati degli errori*/
    if (!$rs)
        die("Errore nella query " . $con->errno . " " . $con->error);
    /*Ciclo di scansione del recordset*/
    if ($rs->num_rows == 0)
        echo ("Nessuna auto individuata");
    else {
        $vect = [];
        while ($record = $rs->fetch_assoc())
            array_push($vect, $record);
        echo (json_encode($vect));
    }
    $con->close();
?>