package de.digitalcollections.iiif.presentation.model.impl.v2;

import de.digitalcollections.iiif.presentation.model.api.v2.Image;
import de.digitalcollections.iiif.presentation.model.api.v2.ImageResource;
import java.net.URI;

public class ImageImpl implements Image {

  private URI id;
  private final String motivation = "sc:painting";
  private URI on;

  private ImageResource resource;
  private final String type = "oa:Annotation";

  public ImageImpl() {
  }

  public ImageImpl(URI id) {
    this.id = id;
  }

  public ImageImpl(String id) {
    this.id = URI.create(id);
  }

  @Override
  public URI getId() {
    return id;
  }

  @Override
  public String getMotivation() {
    return motivation;
  }

  @Override
  public URI getOn() {
    return on;
  }

  @Override
  public void setOn(URI on) {
    this.on = on;
  }

  @Override
  public ImageResource getResource() {
    return resource;
  }

  @Override
  public void setResource(ImageResource resource) {
    this.resource = resource;
  }

  @Override
  public String getType() {
    return type;
  }

}
