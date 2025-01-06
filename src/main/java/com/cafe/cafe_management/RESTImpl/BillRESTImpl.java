package com.cafe.cafe_management.RESTImpl;

import com.cafe.cafe_management.Model.Bill;
import com.cafe.cafe_management.REST.BillREST;
import com.cafe.cafe_management.Service.BillService;
import com.cafe.cafe_management.constants.CafeConstants;
import com.cafe.cafe_management.utils.CafeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BillRESTImpl implements BillREST {

    BillService billService;

    @Autowired
    public BillRESTImpl(BillService billService){
        this.billService = billService;
    }

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        try{
            return billService.generateReport(requestMap);
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        try{
            return billService.getBills();
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        try{
            return billService.getPdf(requestMap);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new byte[0], HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try{
            return billService.deleteBill(id);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
