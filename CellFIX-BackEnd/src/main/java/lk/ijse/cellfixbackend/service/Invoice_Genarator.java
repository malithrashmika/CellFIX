package lk.ijse.cellfixbackend.service;

public interface Invoice_Genarator {
    public byte[] generateInvoicePdf(int repairJobId);
}
