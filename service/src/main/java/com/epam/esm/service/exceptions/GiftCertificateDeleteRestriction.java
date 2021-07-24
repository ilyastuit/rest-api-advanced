package com.epam.esm.service.exceptions;

public class GiftCertificateDeleteRestriction extends Exception{

    public GiftCertificateDeleteRestriction(int id) {
        super("GiftCertificate has orders and can not deleted. <id = "+ id +">");
    }
}
