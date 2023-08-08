package com.hasangurbuz.vehiclemanager;

import com.hasangurbuz.vehiclemanager.domain.Group;
import com.hasangurbuz.vehiclemanager.domain.Vehicle;
import com.hasangurbuz.vehiclemanager.service.GroupService;
import com.hasangurbuz.vehiclemanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VehicleManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehicleManagerApplication.class, args);
    }


}
