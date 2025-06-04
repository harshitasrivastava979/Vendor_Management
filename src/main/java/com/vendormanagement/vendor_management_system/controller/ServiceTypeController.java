import com.vendormanagement.vendor_management_system.dto.ServiceTypeRequestDto;
import com.vendormanagement.vendor_management_system.entity.ServiceType;
import com.vendormanagement.vendor_management_system.service.ServiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/service-types")
public class ServiceTypeController {

    @Autowired
    private ServiceTypeService serviceTypeService;

    // Create a new service type
    @PostMapping
    public ResponseEntity<ServiceType> createServiceType(@RequestBody ServiceTypeRequestDto dto) {
        ServiceType newServiceType = serviceTypeService.createServiceType(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newServiceType);
    }

    // Get all service types
    @GetMapping
    public ResponseEntity<List<ServiceType>> getAllServiceTypes() {
        List<ServiceType> serviceTypes = serviceTypeService.getAllServiceTypes();
        return ResponseEntity.ok(serviceTypes);
    }

    // Get one service type by ID
    @GetMapping("/{id}")
    public ResponseEntity<ServiceType> getServiceTypeById(@PathVariable UUID id) {
        ServiceType serviceType = serviceTypeService.getServiceTypeById(id);
        return ResponseEntity.ok(serviceType);
    }

    // Update existing service type
    @PutMapping("/{id}")
    public ResponseEntity<ServiceType> updateServiceType(@PathVariable UUID id, @RequestBody ServiceTypeRequestDto dto) {
        ServiceType updated = serviceTypeService.updateServiceType(id, dto);
        return ResponseEntity.ok(updated);
    }

    // Delete a service type
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteServiceType(@PathVariable UUID id) {
        serviceTypeService.deleteServiceType(id);
        return ResponseEntity.ok("Service type deleted successfully");
    }



}
