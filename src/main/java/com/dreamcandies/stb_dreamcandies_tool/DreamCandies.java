package com.dreamcandies.stb_dreamcandies_tool;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DreamCandies {
    public static void main(String[] args) {

        //List all the .csv files in one list, so we can access them dynamically
        File myDirectory = new File(
                "C:\\Users\\Dell\\Desktop\\STB_DreamCandies_tool\\csv");
        String[] containingFileNames = myDirectory.list();

        //Create reference variables for the BufferedReaders
        BufferedReader customerSample = null, customer = null, invoice = null, invoiceItem = null;

        HashMap<String, BufferedReader> fileNamesByReaders = new HashMap<>();

        ArrayList<String> customerCodes = new ArrayList<>();
        ArrayList<String> invoiceCodes = new ArrayList<>();

        //Create new .csv files
        try (PrintWriter newCustomer =
                     new PrintWriter("csv\\newCustomer.csv", "UTF-8");
             PrintWriter newInvoice =
                     new PrintWriter("csv\\newInvoice.csv", "UTF-8");
             PrintWriter newInvoiceItem =
                     new PrintWriter("csv\\newInvoiceItem.csv", "UTF-8")) {

            //Map the existing file names and their corresponding BufferedReader
            for (String fileName : containingFileNames) {
                fileNamesByReaders.put(fileName, new BufferedReader(
                        new FileReader(("csv\\" + fileName))));
            }

            customerSample = fileNamesByReaders.get("customer_sample.csv");
            customer = fileNamesByReaders.get("customer.csv");
            invoice = fileNamesByReaders.get("invoice.csv");
            invoiceItem = fileNamesByReaders.get("invoice_item.csv");

            //Read the first line from the files, skip it or add it in the new file as a heading
            customerSample.readLine();
            newCustomer.append(customer.readLine()).append("\n");
            newInvoice.append(invoice.readLine()).append("\n");
            newInvoiceItem.append(invoiceItem.readLine()).append("\n");
            String line = null;

            //Read all data from customer_sample.csv file and put it in customerCodes ArrayList
            while ((line = customerSample.readLine()) != null) {
                customerCodes.add(line);
            }

            //Read all data from customer.csv file. If the customer code is contained in the customerCodes ArrayList, then add this
            //data in the new file
            while ((line = customer.readLine()) != null) {
                String[] customerElements = line.split(",");
                for (String customerCode : customerCodes) {
                    if (customerElements[0].equals(customerCode)) {
                        newCustomer.append(customerElements[0]).append("\t");
                        newCustomer.append(customerElements[1]).append("\t");
                        newCustomer.append(customerElements[2]).append("\n");
                    }
                }
            }

            //Read all data from invoice.csv file. If the customer code is contained in the customerCodes ArrayList, then add this
            //data in the new file. For every contained customer code, put its invoice code in invoiceCode ArrayList.
            while ((line = invoice.readLine()) != null) {
                String[] invoiceElements = line.split(",");
                for (String customerCode : customerCodes) {
                    if (invoiceElements[0].equals(customerCode)) {
                        invoiceCodes.add(invoiceElements[1]);
                        newInvoice.append(invoiceElements[0]).append("\t");
                        newInvoice.append(invoiceElements[1]).append("\t");
                        newInvoice.append(invoiceElements[2]).append("\t");
                        newInvoice.append(invoiceElements[3]).append("\n");
                    }
                }
            }

            //Read all data from invoice_item.csv file. If the invoice code is contained in the invoiceCodes ArrayList, then add this
            //data in the new file.
            while ((line = invoiceItem.readLine()) != null) {
                String[] invoiceItemElements = line.split(",");
                for (String invoiceCode : invoiceCodes) {
                    if (invoiceItemElements[0].equals(invoiceCode)) {
                        newInvoiceItem.append(invoiceItemElements[0]).append("\t");
                        newInvoiceItem.append(invoiceItemElements[1]).append("\t");
                        newInvoiceItem.append(invoiceItemElements[2]).append("\t");
                        newInvoiceItem.append(invoiceItemElements[3]).append("\n");
                    }
                }
            }
        } catch (IOException ioException) {
            System.out.println("Error: " + ioException.getMessage());
            ioException.printStackTrace();
        }
    }
}