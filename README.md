# CompassPlugin

My first project: a minecraft plugin with navigation features on the minecraft world.

Current Implemented Feature:
- Compass on BossBar of the GUI.
  - Compass is degree accurate.
  - Compass has custom colours.
- Includes following commands:
  - Settings Command to toggle and adjust CompassPlugin features.
  - CompassPoints Command to add, remove and view compass points.
    - Different types of compass points: directions and coordinates.
    - Added option to add custom colours to compass points.
- TabCompleters for all commands to facilitate typing commands.
- Permanent saving of user data.
  - Saving of user's settings to "playerSettings.json".
  - Saving of user's compass points to "compassPoints.json".
