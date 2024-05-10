package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore mockPrimaryTS;
  private TorpedoStore mockSecondaryTS;

  @BeforeEach
  public void init() {
    this.mockPrimaryTS = mock(TorpedoStore.class);
    this.mockSecondaryTS = mock(TorpedoStore.class);
    this.ship = new GT4500(mockPrimaryTS, mockSecondaryTS);
  }

  @Test
  public void fireTorpedo_Single_Success() {
    // Arrange
    when(mockPrimaryTS.fire(1)).thenReturn(true);
    when(mockSecondaryTS.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);

    verify(mockPrimaryTS, times(1)).fire(1);
    verify(mockSecondaryTS, times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_All_Success() {
    // Arrange
    when(mockPrimaryTS.fire(1)).thenReturn(true);
    when(mockSecondaryTS.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);

    verify(mockPrimaryTS, times(1)).fire(1);
    verify(mockSecondaryTS, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_PrimaryFirst() {
    // Arrange
    when(mockPrimaryTS.fire(1)).thenReturn(true);
    when(mockSecondaryTS.fire(1)).thenReturn(true);
    
    // Act - fire primary first
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);

    //Verify primary fired
    verify(mockPrimaryTS, times(1)).fire(1);
    verify(mockSecondaryTS, times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_PrimaryFiredLast() {
    // Arrange
    when(mockPrimaryTS.fire(1)).thenReturn(true);
    when(mockSecondaryTS.fire(1)).thenReturn(true);

    // Act - fire primary first
    ship.fireTorpedo(FiringMode.SINGLE);

    // Verify primary fired
    verify(mockPrimaryTS, times(1)).fire(1);
    verify(mockSecondaryTS, times(0)).fire(1);

    // Act - fire secondary after primary
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);

    // Verify both fired
    verify(mockPrimaryTS, times(1)).fire(1);
    verify(mockSecondaryTS, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_PrimaryFiredLast_SecondaryEmpty() {
    // Arrange
    when(mockPrimaryTS.fire(1)).thenReturn(true);
    when(mockSecondaryTS.isEmpty()).thenReturn(true);
    // Act - fire primary first
    ship.fireTorpedo(FiringMode.SINGLE);

    // Verify primary fired
    verify(mockPrimaryTS, times(1)).fire(1);
    verify(mockSecondaryTS, times(0)).fire(1);

    // Act - fire primary again (secondary empty)
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);

    // Verify both fired
    verify(mockPrimaryTS, times(2)).fire(1);
    verify(mockSecondaryTS, times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_PrimaryFiredLast_BothEmpty() {
    // Arrange
    when(mockPrimaryTS.fire(1)).thenReturn(true);
    when(mockSecondaryTS.isEmpty()).thenReturn(true);
    // Act - fire primary first
    ship.fireTorpedo(FiringMode.SINGLE);

    // Verify primary fired
    verify(mockPrimaryTS, times(1)).fire(1);
    verify(mockSecondaryTS, times(0)).fire(1);

    // Arrange - make primary empty
    when(mockPrimaryTS.isEmpty()).thenReturn(true);

    // Act - try to fire again
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(false, result);

    // Verify only primary fired one time
    verify(mockPrimaryTS, times(1)).fire(1);
    verify(mockSecondaryTS, times(0)).fire(1);
  }
 
  @Test
  public void fireTorpedo_Single_BothEmpty() {
    // Arrange
    when(mockPrimaryTS.isEmpty()).thenReturn(true);
    when(mockSecondaryTS.isEmpty()).thenReturn(true);

    // Act - try to fire 
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(false, result);

    // Verify both fired
    verify(mockPrimaryTS, times(0)).fire(1);
    verify(mockSecondaryTS, times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_All_BothEmpty() {
    // Arrange
    when(mockPrimaryTS.isEmpty()).thenReturn(true);
    when(mockSecondaryTS.isEmpty()).thenReturn(true);

    // Act - try to fire 
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(false, result);

    // Verify both fired
    verify(mockPrimaryTS, times(0)).fire(1);
    verify(mockSecondaryTS, times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_SecondaryFiredLast() {
    // Arrange
    when(mockPrimaryTS.fire(1)).thenReturn(true);
    when(mockSecondaryTS.fire(1)).thenReturn(true);

    // Act - fire primary
    ship.fireTorpedo(FiringMode.SINGLE);

    // Verify primary fired
    verify(mockPrimaryTS, times(1)).fire(1);
    verify(mockSecondaryTS, times(0)).fire(1);

    // Act - fire again to make the secondary last
    ship.fireTorpedo(FiringMode.SINGLE);

    // Verify secondary fired
    verify(mockPrimaryTS, times(1)).fire(1);
    verify(mockSecondaryTS, times(1)).fire(1);

    // Act - fire primary after secondary
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);

    // Verify both fired
    verify(mockPrimaryTS, times(2)).fire(1);
    verify(mockSecondaryTS, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_SecondaryFiredLast_PrimaryEmpty() {
    // Arrange
    when(mockPrimaryTS.isEmpty()).thenReturn(true);
    when(mockSecondaryTS.fire(1)).thenReturn(true);
    
    // Act - fire primary first
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);

    //Verify primary fired
    verify(mockPrimaryTS, times(0)).fire(1);
    verify(mockSecondaryTS, times(1)).fire(1);
  }
}
