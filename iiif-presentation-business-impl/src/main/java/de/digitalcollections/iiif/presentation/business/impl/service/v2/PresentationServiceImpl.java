package de.digitalcollections.iiif.presentation.business.impl.service.v2;

import de.digitalcollections.iiif.presentation.backend.api.repository.v2.PresentationRepository;
import de.digitalcollections.iiif.presentation.business.api.PresentationSecurityService;
import de.digitalcollections.iiif.presentation.business.api.v2.PresentationService;
import de.digitalcollections.iiif.presentation.model.api.exceptions.InvalidDataException;
import de.digitalcollections.iiif.presentation.model.api.exceptions.NotFoundException;
import de.digitalcollections.iiif.presentation.model.api.v2.Canvas;
import de.digitalcollections.iiif.presentation.model.api.v2.Collection;
import de.digitalcollections.iiif.presentation.model.api.v2.Manifest;
import de.digitalcollections.iiif.presentation.model.api.v2.Range;
import de.digitalcollections.iiif.presentation.model.api.v2.Sequence;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "PresentationServiceImpl-v2.0.0")
public class PresentationServiceImpl implements PresentationService {

  private static final Logger LOGGER = LoggerFactory.getLogger(PresentationServiceImpl.class);

  @Autowired
  private PresentationRepository presentationRepository;

  @Autowired(required = false)
  private PresentationSecurityService presentationSecurityService;

  @Override
  public Collection getCollection(String name) throws NotFoundException, InvalidDataException {
    try {
      return presentationRepository.getCollection(name);
    } catch (de.digitalcollections.iiif.presentation.model.api.exceptions.NotFoundException ex) {
      LOGGER.debug("Collection for '{}' not found.", name);
      throw new NotFoundException(ex.getMessage());
    }
  }

  @Override
  public Manifest getManifest(String identifier) throws NotFoundException, InvalidDataException {
    if (presentationSecurityService != null && !presentationSecurityService.isAccessAllowed(identifier)) {
      LOGGER.info("Access to manifest for object '{}' is not allowed!", identifier);
      throw new NotFoundException(); // TODO maybe throw an explicitely access disallowed exception
    }
    LOGGER.debug("Access to manifest for object '{}' is allowed.", identifier);
    try {
      return presentationRepository.getManifest(identifier);
    } catch (de.digitalcollections.iiif.presentation.model.api.exceptions.NotFoundException ex) {
      LOGGER.debug("Manifest for '{}' not found.", identifier);
      throw new NotFoundException(ex.getMessage());
    }
  }

  @Override
  public Canvas getCanvas(String manifestId, URI canvasUri) throws NotFoundException, InvalidDataException {
    Manifest manifest = getManifest(manifestId);
    return manifest.getSequences().stream()
        .flatMap(seq -> seq.getCanvases().stream())
        .filter(canv -> canv.getId().equals(canvasUri))
        .findFirst().orElseThrow(NotFoundException::new);
  }

  @Override
  public Range getRange(String manifestId, URI rangeUri) throws NotFoundException, InvalidDataException {
    Manifest manifest = getManifest(manifestId);
    return manifest.getStructures().stream()
        .filter(range -> range.getId().equals(rangeUri))
        .findFirst().orElseThrow(NotFoundException::new);
  }

  @Override
  public Sequence getSequence(String manifestId, URI sequenceUri) throws NotFoundException, InvalidDataException {
    Manifest manifest = getManifest(manifestId);
    return manifest.getSequences().stream()
        .filter(seq -> seq.getId().equals(sequenceUri))
        .findFirst().orElseThrow(NotFoundException::new);
  }
}
