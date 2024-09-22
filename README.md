### Instructions for Using the Game Recommendation System

1. **Launch the Program**
   - Run the system from the command line by executing the `Main` class.
   - 
   - You will be prompted to enter your preferences for game recommendations.

2. **Provide User Input**
   - When prompted, enter your preferences in the following format:
     ```
     Genre; game mode; platform
     ```
     - **Genre**: Specify one or more genres separated by commas (e.g., `rpg, fps, adventure`). If you want the system to consider any genre, enter `any`.
     - **Game Mode**: Specify the game mode, for example, `multi` or `solo`.
     - **Platform**: Specify the platform (e.g., `pc`, `ps4`). If any platform works for you, enter `any`.

   Example input:
   ```
   prg, adventure; solo; pc
   ```

3. **Receive Recommendations**
   - Based on your input, the system will perform a query on the ontology and return game recommendations.
   - The output will display the names of games that match your preferences, along with the number of online players (if applicable).

4. **System Commands**
   - **`help`**: Displays a help message explaining the input format and commands.
   - **`exit`**: Exits the system.

5. **Errors and Warnings**
   - If your input doesn't match the expected format or cannot be parsed, the system will prompt you to re-enter the information.