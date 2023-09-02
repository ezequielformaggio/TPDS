
    create table Administrador (
        idAdministrador bigint identity not null,
        contrasenia varchar(255),
        usuario varchar(255),
        primary key (idAdministrador)
    )

    create table Contacto (
        idContacto bigint identity not null,
        mail varchar(255),
        telefono varchar(255),
        idOrganizacion bigint,
        primary key (idContacto)
    )

    create table Medicion (
        idMedicion bigint identity not null,
        periodicidad varchar(255),
        periodoDeImputacion date,
        valor double precision,
        idTipoDeConsumo bigint,
        idOrganizacion bigint,
        primary key (idMedicion)
    )

    create table MedioDeTransporte (
        tipoDeTransporte varchar(31) not null,
        idMedioDeTransporte bigint identity not null,
        consumoPorKM double precision,
        nombre varchar(255),
        combustible varchar(255),
        tipoVehiculo varchar(255),
        tipoTransporteEcologico varchar(255),
        linea varchar(255),
        tipoTransportePublico varchar(255),
        primary key (idMedioDeTransporte)
    )

    create table MetodoNotificacion (
        idContacto bigint not null,
        tipoNotificacion varchar(255)
    )

    create table Miembro (
        idMiembro bigint identity not null,
        apellido varchar(255),
        nombre varchar(255),
        nroDocumento varchar(255),
        tipoDoc varchar(255),
        primary key (idMiembro)
    )

    create table Organizacion (
        idOrganizacion bigint identity not null,
        clasificacion varchar(255),
        diasHabiles int not null,
        altura int not null,
        calle varchar(255),
        localidad int not null,
        razonSocial varchar(255),
        tipoOrg varchar(255),
        idSectorTerritorial bigint,
        primary key (idOrganizacion)
    )

    create table Parada (
        idParada bigint identity not null,
        altura int not null,
        calle varchar(255),
        localidad int not null,
        distanciaProxima double precision not null,
        nombre varchar(255),
        idMedioDeTransporte bigint,
        primary key (idParada)
    )

    create table RelacionMiembrosSectores (
        idMiembro bigint not null,
        idSector bigint not null
    )

    create table RelacionMiembrosTrayectos (
        idMiembro bigint not null,
        idTrayecto bigint not null
    )

    create table RelacionTramosMiembros (
        idTramo bigint not null,
        idMiembro bigint not null
    )

    create table Sector (
        idSector bigint identity not null,
        descripcion varchar(255),
        idOrganizacion bigint,
        primary key (idSector)
    )

    create table SectorTerritorial (
        idSectorTerritorial bigint identity not null,
        descripcion varchar(255),
        primary key (idSectorTerritorial)
    )

    create table SolicitudVinculacion (
        idSolicitudVinculacion bigint identity not null,
        estado varchar(255),
        idMiembro bigint,
        idSector bigint,
        idOrganizacion bigint,
        primary key (idSolicitudVinculacion)
    )

    create table TipoDeConsumo (
        idTipoDeConsumo bigint identity not null,
        actividad varchar(255),
        alcance varchar(255),
        descripcion varchar(255),
        coeficienteFactorEmision double precision,
        unidadDenominador varchar(255),
        unidadNumerador varchar(255),
        unidadTipoDeConsumo varchar(255),
        primary key (idTipoDeConsumo)
    )

    create table Tramo (
        idTramo bigint identity not null,
        alturaDestino int,
        calleDestino varchar(255),
        localidadDestino int,
        idTrayecto bigint,
        alturaOrigen int,
        calleOrigen varchar(255),
        localidadOrigen int,
        idMedioTransporte bigint,
        primary key (idTramo)
    )

    create table Trayecto (
        idTrayecto bigint identity not null,
        descripcion varchar(255),
        primary key (idTrayecto)
    )

    alter table Contacto 
        add constraint FK_nslpb3q9wnhp3dxrtyhay6ef1 
        foreign key (idOrganizacion) 
        references Organizacion

    alter table Medicion 
        add constraint FK_sv7j4h2llbmxxo7wbhaygygwx 
        foreign key (idTipoDeConsumo) 
        references TipoDeConsumo

    alter table Medicion 
        add constraint FK_hm68b214wttux86snoobe1kpg 
        foreign key (idOrganizacion) 
        references Organizacion

    alter table MetodoNotificacion 
        add constraint FK_bu25ua2wptb6tk5j02fbmkew1 
        foreign key (idContacto) 
        references Contacto

    alter table Organizacion 
        add constraint FK_jkics5s3ip6e6b27k1mr9hi5k 
        foreign key (idSectorTerritorial) 
        references SectorTerritorial

    alter table Parada 
        add constraint FK_e5fqcve7k9l37d44l4aw8jxer 
        foreign key (idMedioDeTransporte) 
        references MedioDeTransporte

    alter table RelacionMiembrosSectores 
        add constraint FK_aktb2n5j4v2ia13bwihixnk6w 
        foreign key (idSector) 
        references Sector

    alter table RelacionMiembrosSectores 
        add constraint FK_d2esm4a5ld4jnlllv7bfgl4x 
        foreign key (idMiembro) 
        references Miembro

    alter table RelacionMiembrosTrayectos 
        add constraint FK_mdci86dlqgww9ufn72kgyhaex 
        foreign key (idTrayecto) 
        references Trayecto

    alter table RelacionMiembrosTrayectos 
        add constraint FK_7d7pgo4476pqyi3h971f4m064 
        foreign key (idMiembro) 
        references Miembro

    alter table RelacionTramosMiembros 
        add constraint FK_bkytcvnclpkt5ixf6tsvf6eiy 
        foreign key (idMiembro) 
        references Miembro

    alter table RelacionTramosMiembros 
        add constraint FK_odm5c5ux6wxgp4m4sgmvg4ek6 
        foreign key (idTramo) 
        references Tramo

    alter table Sector 
        add constraint FK_dsywj4oq69lnicfw8fb428f5w 
        foreign key (idOrganizacion) 
        references Organizacion

    alter table SolicitudVinculacion 
        add constraint FK_a79fro6re8jbdrw6wk25g4iki 
        foreign key (idMiembro) 
        references Miembro

    alter table SolicitudVinculacion 
        add constraint FK_iiqmfosf3y1yn2cvhj7e78lwn 
        foreign key (idSector) 
        references Sector

    alter table SolicitudVinculacion 
        add constraint FK_p52e6agbo80j9t00idrfsdora 
        foreign key (idOrganizacion) 
        references Organizacion

    alter table Tramo 
        add constraint FK_t96xestmhvxofc6c299vsaact 
        foreign key (idMedioTransporte) 
        references MedioDeTransporte

    alter table Tramo 
        add constraint FK_25me72rlxaoh42bj4g65gi3qf 
        foreign key (idTrayecto) 
        references Trayecto
