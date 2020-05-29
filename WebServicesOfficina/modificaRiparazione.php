<?php
    /*Questo WS modifica una riparazione presente sul DB */
    /*Accetta come parametri idRip, idAuto, dataRiparazione, causaRiparazione, costoRiparazione e pagatoRiparazione */
    $con = new mysqli("localhost", "root", "", "officina");
    /*Controlla se il codice di errore è diverso da 0*/
    if ($con->connect_errno)
        die("Errore connessione database " . $con->connect_errno . " " . $con->connect_error);
    if (!isset($_POST["idRip"]))
        die("Codice Riparazione Mancante");
    else
        $idRip = $_POST["idRip"];
    if(!isset($_POST["idAuto"]))
        die("Codice Auto Mancante");
    else
        $idAuto = $_POST["idAuto"];

    if (!isset($_POST["dataRiparazione"]))
        die("Data Riparazione Mancante");
    else
        $dataRip = $_POST["dataRiparazione"];

    if (!isset($_POST["causaRiparazione"]))
        die("Causa Riparazione Mancante");
    else
        $causaRip = $_POST["causaRiparazione"];

    if (!isset($_POST["costoRiparazione"]))
        die("Costo Riparazione Mancante");
    else
        $costoRip = $_POST["costoRiparazione"];

    if (!isset($_POST["pagatoRiparazione"]))
        die("Stato Pagamento Riparazione Mancante");
    else
    {
        if($_POST["pagatoRiparazione"] == "false")
            $pagatoRip = 0;
        else
            $pagatoRip = 1;
    }
        

    $sql = "UPDATE riparazioni SET idAuto = $idAuto, data = '$dataRip', causa = '$causaRip', costo = $costoRip, pagato = $pagatoRip WHERE idRip = $idRip";
    /*Il metodo query lancia la query sql e restituisce il recordset corrispondente*/
    $rs = $con->query($sql);
    /*Controlla se il recordset esiste o no cioè se ci sono stati degli errori*/
    if (!$rs)
        die("Errore nella query " . $con->errno . " " . $con->error);
    else 
        echo("ModRiparazioneOk");
    $con->close();
?>