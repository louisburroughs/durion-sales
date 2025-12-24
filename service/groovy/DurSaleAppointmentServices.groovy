// Groovy service implementations for DurSaleAppointmentServices

def checkAppointmentAvailability() {
    def appointmentDate = context.appointmentDate
    def duration = context.duration ?: 60
    def technicianId = context.assignedTechnicianId
    
    // Query existing appointments for the date/time range
    def conflicts = ec.entity.find("durion.sales.DurSaleAppointment")
            .condition("appointmentDate", appointmentDate)
            .condition("assignedTechnicianId", technicianId)
            .condition("status", "confirmed")
            .list()
    
    def isAvailable = conflicts.isEmpty()
    
    return [
        isAvailable: isAvailable,
        conflicts: conflicts
    ]
}

def assignTechnicianToAppointment() {
    def appointmentId = context.appointmentId
    def technicianId = context.technicianId
    
    // Check availability first
    def appointment = ec.entity.find("durion.sales.DurSaleAppointment")
            .condition("appointmentId", appointmentId).one()
    
    def availabilityCheck = checkAppointmentAvailability([
        appointmentDate: appointment.appointmentDate,
        assignedTechnicianId: technicianId,
        duration: appointment.duration
    ])
    
    if (availabilityCheck.isAvailable) {
        appointment.assignedTechnicianId = technicianId
        appointment.update()
        return [success: true]
    } else {
        return [success: false, message: "Technician not available at that time"]
    }
}
