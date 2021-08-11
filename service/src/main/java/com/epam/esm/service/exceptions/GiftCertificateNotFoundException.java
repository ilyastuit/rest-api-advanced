package com.epam.esm.service.exceptions;

public class GiftCertificateNotFoundException extends Exception {
    public GiftCertificateNotFoundException(int id) {
        super("GiftCertificate is not found (id = " + id + ")");
    }
}
