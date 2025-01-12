package com.example.datenbankv5.ToDoComponent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datenbankv5.R;
import com.example.datenbankv5.ToDoComponent.core.Priority;
import com.example.datenbankv5.ToDoComponent.core.Task;

import java.util.List;

/**
 * Adapter für die Anzeige von Aufgaben in einer RecyclerView.
 * Bindet Aufgaben-Daten an die benutzerdefinierte Layout-Darstellung für jede Aufgabe.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private List<Task> taskList; // Liste zur Speicherung der Aufgaben-Daten

    /**
     * Konstruktor für den Adapter.
     *
     * @param context Der Kontext, in dem der Adapter verwendet wird.
     * @param taskList Die Liste von Aufgaben, die im RecyclerView angezeigt werden.
     */
    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    /**
     * Wird aufgerufen, um eine neue ViewHolder-Instanz zu erstellen.
     *
     * @param parent Der ViewGroup, zu der die ViewHolder-Instanz gehört.
     * @param viewType Der Typ der View, die erstellt werden soll.
     * @return Eine neue Instanz von TaskViewHolder.
     */
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Das Layout für jedes Listenelement aufblähen
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    /**
     * Bindet die Aufgaben-Daten an die View-Elemente des ViewHolders.
     *
     * @param holder Der ViewHolder, der mit der Ansicht verbunden wird.
     * @param position Die Position der Aufgabe in der Liste.
     */
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        // Setzt den Titel der Aufgabe
        holder.textViewTask.setText(task.getTask());

        // Setzt die Priorität
        holder.textViewPriority.setText("Priorität: " + getPriorityText(task.getPriority()));

        // Setzt die Kategorie, falls vorhanden
        if (task.getCategory() != null) {
            holder.textViewCategory.setText("Kategorie: " + task.getCategory().getName());
            holder.textViewCategory.setVisibility(View.VISIBLE);
        } else {
            holder.textViewCategory.setVisibility(View.GONE);
        }

        // Setzt die Beschreibung, falls vorhanden
        if (task.getDescription() != null && !task.getDescription().isEmpty()) {
            holder.textViewDescription.setText(task.getDescription());
            holder.textViewDescription.setVisibility(View.VISIBLE);
        } else {
            holder.textViewDescription.setVisibility(View.GONE);
        }
    }

    /**
     * Gibt den Text der Priorität einer Aufgabe basierend auf deren Prioritäts-Enum zurück.
     *
     * @param priority Die Priorität der Aufgabe.
     * @return Der Text, der die Priorität beschreibt.
     */
    private String getPriorityText(Priority priority) {
        // Handhabt Priorität mit Enums
        switch (priority) {
            case URGENT_IMPORTANT:
                return "Wichtig & Dringend";
            case NOT_URGENT_IMPORTANT:
                return "Wichtig & Nicht Dringend";
            case URGENT_NOT_IMPORTANT:
                return "Nicht Wichtig & Dringend";
            case NOT_URGENT_NOT_IMPORTANT:
                return "Nicht Wichtig & Nicht Dringend";
            default:
                return "Unbekannt";
        }
    }

    /**
     * Gibt die Anzahl der Elemente in der Liste zurück.
     *
     * @return Die Anzahl der Aufgaben im RecyclerView.
     */
    @Override
    public int getItemCount() {
        return taskList.size(); // Anzahl der Elemente in der Liste
    }

    /**
     * ViewHolder-Klasse für einzelne Aufgaben-Elemente im RecyclerView.
     */
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTask, textViewCategory, textViewPriority, textViewDescription;

        /**
         * Konstruktor für den ViewHolder. Initialisiert die View-Elemente.
         *
         * @param itemView Die Ansicht des Listenelements.
         */
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTask = itemView.findViewById(R.id.textViewTask);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewPriority = itemView.findViewById(R.id.textViewPriority);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
        }
    }
}
