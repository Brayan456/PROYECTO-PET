package com.petfrendly.alimentador.consumidor;




import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.petfrendly.alimentador.Entidades.Mascota;
import com.petfrendly.alimentador.MascotaRepository;
import com.petfrendly.alimentador.servicio.EmailService;

import java.util.Optional;

@Service
public class MascotaPerdidaConsumer {

    private final MascotaRepository mascotaRepository;
    private final EmailService emailService;

    public MascotaPerdidaConsumer(MascotaRepository mascotaRepository, EmailService emailService) {
        this.mascotaRepository = mascotaRepository;
        this.emailService = emailService;
    }

    @KafkaListener(topics = "mascota_perdida", groupId = "perreras_group")
    public void consumirEvento(String mensaje) {
        // Dividir el mensaje por comas
        String[] partes = mensaje.split(",");
        String idMascota = partes[0];
        String latitud = partes[1];
        String longitud = partes[2];
        Optional<Mascota> mascotaOpt = mascotaRepository.findById(Long.parseLong(idMascota));

        if (mascotaOpt.isPresent()) {
            Mascota mascota = mascotaOpt.get();
            String correoDueño = mascota.getDueno().getContacto();  // Aquí el teléfono es el correo
            String asunto = "Mascota Perdida: " + mascota.getNombre();
            String contenido = "<h1>¡Aviso de Mascota Perdida!</h1>" +
                    "<p>Tu mascota <strong>" + mascota.getNombre() + "</strong> ha sido reportada como perdida.</p>" +
                    "<p>Ubicación: : Latitud = " + latitud + ", Longitud = " + longitud + "</p>";


            emailService.enviarCorreo(correoDueño, asunto, contenido);
        } else {
            System.out.println("No se encontró ninguna mascota con el ID: " + idMascota);
        }
    }
}

