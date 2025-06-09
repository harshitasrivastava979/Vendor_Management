package com.vendormanagement.vendor_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceTypeRequestDto {
        private UUID id ;
        private String name;
       // private String description;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public UUID getId() {
                return id;
        }

        public void setId(UUID id) {
                this.id = id;
        }
}
