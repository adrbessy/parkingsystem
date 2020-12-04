package com.parkit.parkingsystem.util;

import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InputReaderUtilTest {

  @Mock
  Scanner scan;

  InputReaderUtil inputReaderUtil;

  @BeforeEach
  public void setUp() {
    inputReaderUtil = new InputReaderUtil();
  }

  /*
   * @Test public void readSelectionTest() { // GIVEN Scanner scan =
   * mock(Scanner.class); when(scan.nextLine()).thenReturn("1");
   * 
   * // WHEN final int result = inputReaderUtil.readSelection();
   * 
   * // THEN verify(scan, times(1)).nextLine(); assertThat(result).isEqualTo(1); }
   */

}
