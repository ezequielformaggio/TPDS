package db;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import interesadosguiarecomendaciones.InteresadoMail;
import interesadosguiarecomendaciones.InteresadoWhatsApp;
import mailsender.MailSender;
import medicion.*;
import miembro.Miembro;
import miembro.TipoDocumento;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import organizacion.*;
import organizacion.sectorterritorial.SectorTerritorial;
import tareascalendarizadas.TareaCalendarizadaApp;
import whatsappsender.WhatsAppSender;

import java.time.LocalDate;
import java.util.List;

public class ContextTest extends AbstractPersistenceTest implements WithGlobalEntityManager {

	@Test
	public void contextUp() {
		assertNotNull(entityManager());
	}

	@Test
	public void contextUpWithTransaction() throws Exception {
		withTransaction(() -> {});
	}
}
